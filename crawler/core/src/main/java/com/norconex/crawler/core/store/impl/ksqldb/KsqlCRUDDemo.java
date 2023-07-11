package com.norconex.crawler.core.store.impl.ksqldb;

import io.confluent.ksql.api.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.common.utils.Utils.sleep;

public class KsqlCRUDDemo {
    private static Client client;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ClientOptions options = ClientOptions.create()
                .setHost("localhost")
                .setPort(8088);
        client = Client.create(options);

        // Query by key.
        queryByKeyExample();

        // Insert/update record example
        insertRecordExample();

        // Delete record (set value to null);
        deleteRecord();

        // Select all
        sleep(5000);
        selectAll();

        // Exists query
        existsQuery();

        client.close();
    }

    private static void existsQuery() {
        String pullQuery = "SELECT 1 FROM QGRADES WHERE ID=6;";
        BatchedQueryResult batchedQueryResult = client.executeQuery(pullQuery);

        List<Row> resultRows;
        try {
            resultRows = batchedQueryResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println(resultRows.size() > 0);
    }

    private static void deleteRecord() {
        KsqlObject doc = new KsqlObject();

        doc.put("ID", 3);
//        doc.put("GRADE", "A");
//        doc.put("RANK", 3);

//        client.insertInto("GRADES", doc);

        List<KsqlObject> rows = new ArrayList<>();
        rows.add(doc);
        CompletableFuture<Void> result = CompletableFuture.allOf(
                rows.stream()
                        .map(row -> client.insertInto("GRADES", row))
                        .toArray(CompletableFuture[]::new)
        );
    }

    private static void selectAll() throws ExecutionException, InterruptedException {
        String pullQuery = "SELECT * FROM QGRADES;";
        BatchedQueryResult batchedQueryResult = client.executeQuery(pullQuery);

        // Wait for query result
        List<Row> resultRows = batchedQueryResult.get();

        System.out.println("Received results. Num rows: " + resultRows.size());
        for (Row row : resultRows) {
            System.out.println("Row: " + row.values());
        }
    }

    private static void insertRecordExample() {
        KsqlObject doc = new KsqlObject();

        doc.put("ID", 3);
        doc.put("GRADE", "A");
        doc.put("RANK", 7);

//        client.insertInto("GRADES", doc);

        List<KsqlObject> rows = new ArrayList<>();
        rows.add(doc);
        CompletableFuture<Void> result = CompletableFuture.allOf(
                rows.stream()
                        .map(row -> client.insertInto("GRADES", row))
                        .toArray(CompletableFuture[]::new)
        );
    }

    private static void queryByKeyExample() throws ExecutionException, InterruptedException {
        String id = "5";
        String pullQuery = "SELECT * FROM QGRADES WHERE ID=" + id + ";";
        BatchedQueryResult batchedQueryResult = client.executeQuery(pullQuery);

        // Wait for query result
        List<Row> resultRows = batchedQueryResult.get();

        System.out.println("Received results. Num rows: " + resultRows.size());
        for (Row row : resultRows) {
            System.out.println("Row: " + row.values());
        }
    }
}
