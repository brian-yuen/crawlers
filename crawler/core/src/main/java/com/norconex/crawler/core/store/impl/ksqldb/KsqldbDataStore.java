package com.norconex.crawler.core.store.impl.ksqldb;

import com.google.gson.Gson;
import com.norconex.crawler.core.store.DataStore;
import com.norconex.crawler.core.store.impl.SerialUtil;
import io.confluent.ksql.api.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiPredicate;

import static java.util.Objects.requireNonNull;

public class KsqldbDataStore<T> implements DataStore<T> {
    private static final Logger LOG = LoggerFactory.getLogger(KsqldbDataStore.class);
    private static final String ID = "ID";
    private static final String VALUE = "VALUE";
    private String name;
    private String dropDerivedTableQuery;

    private Client client;
    private String tableName;
    private String queryableTableName;
    private final Class<? extends T> type;

    private String INSERT_QUERY = "";
    private String UPDATE_QUERY = "";
    private String retrieveQuery;
    private String retrieveFirstDocQuery;
    private String existsQuery;
    private String dropTableQuery;
    private String createTableQuery;
    private String createDerivedTableQuery;

    private static Gson gson;

    private KsqldbDataStoreEngine engine;

    public KsqldbDataStore(KsqldbDataStoreEngine engine, String ksqldbServerHost, int ksqldbServerPort,
                           String tableName, Class<? extends T> type, int partitions) {
        this.name = requireNonNull(tableName, "'tableName' must not be null.");
        this.tableName = tableName;
        this.type = type;
        queryableTableName = "QUERYABLE_" + tableName;
        gson = new Gson();
        this.engine = engine;

        ClientOptions options = ClientOptions.create()
                .setHost(ksqldbServerHost)
                .setPort(ksqldbServerPort);
        client = Client.create(options);

        retrieveQuery = "SELECT * FROM " + queryableTableName + " WHERE ID=";
        retrieveFirstDocQuery = "SELECT * FROM " + queryableTableName + " LIMIT 1";
        existsQuery = "SELECT 1 FROM " + queryableTableName + " WHERE ID=";
//        dropDerivedTableQuery = "DROP TABLE " + queryableTableName + ";";
//        dropTableQuery = "DROP TABLE " + tableName + ";";

        createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (ID STRING PRIMARY KEY, VALUE STRING) " +
                "WITH (KAFKA_TOPIC='" + tableName + "', VALUE_FORMAT='json', PARTITIONS=" + partitions + ");";

        createDerivedTableQuery = "CREATE TABLE IF NOT EXISTS " + queryableTableName +
                " AS SELECT * FROM " + tableName + " WHERE VALUE IS NOT NULL;";

        createTables();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void save(String id, Object object) {
        String value = SerialUtil.toJsonString(object);

        if (id.isBlank() || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Both parameters must have values");
        }

        LOG.trace("Adding to Ksqldb. Table={}, id={}, value={}", tableName, id, value);
        saveOrUpdate(id, value);
    }

    private void saveOrUpdate(String id, String value) {
        KsqlObject doc = new KsqlObject();
        doc.put(ID, id);
        doc.put(VALUE, value);
        try {
//            client.insertInto(tableName, doc)
            client.insertInto(tableName, doc).get(3000l, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> find(String id) {
        return findDoc(retrieveQuery + "'" + id + "'");
    }

    private List<Row> getResults(String query) {
        query = query + ";";
        BatchedQueryResult batchedQueryResult = client.executeQuery(query);

        List<Row> resultRows;
        try {
            resultRows = batchedQueryResult.get();
            return resultRows;
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while executing read query:" + query);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> findFirst() {
        return findDoc(retrieveFirstDocQuery);
    }

    private Optional<T> findDoc(String query) {
        List<Row> resultRows = getResults(query);

        if (resultRows.size() > 0) {
            Row row = resultRows.get(0);
//            return Optional.ofNullable(SerialUtil.fromJson(row.getString(VALUE), type));
            return Optional.ofNullable(gson.fromJson(row.getString(VALUE), type));
        }

        return Optional.empty();
    }

    @Override
    public boolean exists(String id) {
        List<Row> results = getResults(existsQuery + "'" + id + "'");
        return results != null && results.size() > 0;
    }

    @Override
    public long count() {
        // TODO: Need to R&D how to implement this.
//        SELECT 'X' AS X,
//        COUNT(*) AS MSG_CT
//        FROM PAGEVIEWS
//        GROUP BY 'X'
//        EMIT CHANGES LIMIT 1;
        return 0;
    }

    @Override
    public boolean delete(String id) {
        if (id.isBlank()) {
            throw new IllegalArgumentException(
                    "Id must have value.");
        }

        LOG.trace("Deleting from Ksqldb. Table={}, id={}", tableName, id);
        saveOrUpdate(id, null);

        return false;
    }

    @Override
    public Optional<T> deleteFirst() {
        List<Row> resultRows = getResults(retrieveFirstDocQuery);

        if (resultRows.size() > 0) {
            Row row = resultRows.get(0);
            String id = row.getString(ID);
            saveOrUpdate(id, null);
            return Optional.ofNullable(gson.fromJson(row.getString(VALUE), type));
        }

        return Optional.empty();
    }

    @Override
    public void clear() {
        dropTables();
        createTables();
    }

    private void dropTables() {
        dropTable(queryableTableName);
        dropTable(tableName);
        delay();
    }

    private void dropTable(String tableName) {
        try {
            client.executeStatement("DROP TABLE " + tableName + ";").get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while dropping table: " + tableName);
            throw new RuntimeException(e);
        }
    }

    private void createTables() {
        createTableFromQuery(createTableQuery);
        createTableFromQuery(createDerivedTableQuery);
        delay();
    }

    private void createTableFromQuery(String query) {
        try {
            client.executeStatement(query).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while creating table using query: " + query);
            throw new RuntimeException(e);
        }
    }

    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        //TODO: Also need to stop queries and drop tables.
        dropTables();
        client.close();
    }

    @Override
    public boolean isEmpty() {
        // TODO: This method depends on count method.
        return false;
    }

    @Override
    public boolean forEach(BiPredicate predicate) {
        return false;
    }

    boolean rename(String newStoreName) {
        var newTableName = engine.tableName(newStoreName);
        var targetExists = engine.tableExist(newTableName);
        if (targetExists) {
            dropTable(newTableName);
        }

        // TODO: Need to use string format in all queries and here need to find
        // how we will rename table because ksqldb doesn't have that feature and better would be
        // not renaming table in case of ksqldb data store, as it will be computationally intensive task,
        // because we have to create new table manually and drop old table, which is not feasible.
        createTableFromQuery(newTableName);

        this.name = newStoreName;
        this.tableName = newTableName;
        return targetExists;
    }

    Class<?> getType() {
        return type;
    }
}
