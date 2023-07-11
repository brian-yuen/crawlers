package com.norconex.crawler.core.store.impl.ksqldb;

import com.norconex.commons.lang.xml.XML;
import com.norconex.commons.lang.xml.XMLConfigurable;
import com.norconex.crawler.core.crawler.Crawler;
import com.norconex.crawler.core.store.DataStore;
import com.norconex.crawler.core.store.DataStoreEngine;
import com.norconex.crawler.core.store.DataStoreException;
import com.norconex.crawler.core.store.impl.mongodb.MongoDataStoreEngine;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import io.confluent.ksql.api.client.TableInfo;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.removeStartIgnoreCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

public class KsqldbDataStoreEngine implements DataStoreEngine, XMLConfigurable {
    private static final Logger LOG =
            LoggerFactory.getLogger(KsqldbDataStoreEngine.class);
    private static final String STORE_TYPES_KEY =
            KsqldbDataStoreEngine.class.getSimpleName() + "--storetypes";
    private String ksqldbServerHost;
    private int ksqldbServerPort;
    private int partitions;
    private int replicas;
    private String tablePrefix;
    private KsqldbDataStore<String> storeTypes;
    private Client ksqldbClient;

    @Override
    public void init(Crawler crawler) {
        LOG.info("Initializing KsqlDB data store engine...");

        if (tablePrefix == null) {
            tablePrefix = tablePrefix(crawler.getCrawlSession().getId()
                    + "_" + crawler.getId() + "_");
        }

        ClientOptions options = ClientOptions.create()
                .setHost(ksqldbServerHost)
                .setPort(ksqldbServerPort);
        ksqldbClient = Client.create(options);

        storeTypes = new KsqldbDataStore<>(
                this, ksqldbServerHost, ksqldbServerPort,
                STORE_TYPES_KEY, String.class, 1);
        LOG.info("KsqlDB data store engine initialized.");
    }

    @Override
    public boolean clean() {
        // the table storing the store types is not returned by getStoreNames
        // so we have to explicitly delete it.
        var names = getStoreNames();
        var hasStores = !names.isEmpty();
        if (hasStores) {
            names.stream().forEach(this::dropStore);
        }
        dropStore(STORE_TYPES_KEY);
        return hasStores;
    }

    @Override
    public void close() {
        if (ksqldbClient != null) {
            LOG.info("Closing KsqlDB data store engine datasource...");
            ksqldbClient.close();
            LOG.info("KsqlDB Data store engine datasource closed.");
            ksqldbClient = null;
        } else {
            LOG.info("KsqlDB Data store engine datasource already closed.");
        }
    }

    @Override
    public <T> DataStore<T> openStore(String storeName, Class<? extends T> type) {
        storeTypes.save(storeName, type.getName());
        return new KsqldbDataStore<>(this, ksqldbServerHost, ksqldbServerPort, storeName, type, partitions);
    }

    @Override
    public boolean dropStore(String storeName) {
        var tableName = tableName(storeName);
        if (!tableExist(tableName)) {
            return false;
        }

        try {
            ksqldbClient.executeStatement("DROP TABLE " + tableName + ";").get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while dropping table " + tableName);
            throw new RuntimeException(e);
        }

        if (STORE_TYPES_KEY.equals(storeName)) {
            storeTypes = null;
        } else {
            storeTypes.delete(storeName);
        }
        return true;
    }

    boolean tableExist(String tableName) {
        List<TableInfo> tables = retrieveTablesInfo();

        for (TableInfo table : tables) {
            if (tableName.equalsIgnoreCase(table.getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean renameStore(DataStore<?> dataStore, String newStoreName) {
        KsqldbDataStore<?> ksqldbDataStore = (KsqldbDataStore<?>) dataStore;
        var oldStoreName = ksqldbDataStore.getName();
        var existed = ((KsqldbDataStore<?>) dataStore).rename(newStoreName);
        storeTypes.delete(oldStoreName);
        storeTypes.save(newStoreName, ksqldbDataStore.getType().getName());
        return existed;
    }

    @Override
    public Set<String> getStoreNames() {
        Set<String> names = new HashSet<>();
        List<TableInfo> tables = retrieveTablesInfo();

        for (TableInfo table : tables) {
            if (!startsWithIgnoreCase(table.getName(), tablePrefix))
                continue;

            var storeName = removeStartIgnoreCase(table.getName(), tablePrefix);
            if (!STORE_TYPES_KEY.equalsIgnoreCase(storeName)) {
                names.add(storeName);
            }
        }

        return names;
    }

    private List<TableInfo> retrieveTablesInfo() {
        try {
            return ksqldbClient.listTables().get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error while getting tables info from KsqlDB");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Class<?>> getStoreType(String storeName) {
        if (storeName == null) {
            return Optional.empty();
        }
        var typeStr = storeTypes.find(storeName);
        if (typeStr.isPresent()) {
            try {
                return Optional.ofNullable(ClassUtils.getClass(typeStr.get()));
            } catch (ClassNotFoundException e) {
                throw new DataStoreException(
                        "Could not determine type of: " + storeName, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public void loadFromXML(XML xml) {
        setKsqldbServerHost(xml.getString("host", getKsqldbServerHost()));
        setKsqldbServerPort(xml.getInteger("port", getKsqldbServerPort()));
        setPartitions(xml.getInteger("partitions", getPartitions()));
        setReplicas(xml.getInteger("replicas", getReplicas()));
    }

    @Override
    public void saveToXML(XML xml) {
        xml.addElement("host", getKsqldbServerHost());
        xml.addElement("port", getKsqldbServerPort());
        xml.addElement("partitions", getPartitions());
        xml.addElement("replicas", getReplicas());
    }

    public String getKsqldbServerHost() {
        return ksqldbServerHost;
    }

    public void setKsqldbServerHost(String ksqldbServerHost) {
        this.ksqldbServerHost = ksqldbServerHost;
    }

    public int getKsqldbServerPort() {
        return ksqldbServerPort;
    }

    public void setKsqldbServerPort(int ksqldbServerPort) {
        this.ksqldbServerPort = ksqldbServerPort;
    }

    String tablePrefix(String prefix) {
        var n = prefix.trim();
        n = n.replaceFirst("(?i)^[^a-z]", "x");
        return n.replaceAll("\\W+", "_");
    }

    String tableName(String storeName) {
        var n = tablePrefix + storeName.trim();
        return n.replaceAll("\\W+", "_");
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }
}
