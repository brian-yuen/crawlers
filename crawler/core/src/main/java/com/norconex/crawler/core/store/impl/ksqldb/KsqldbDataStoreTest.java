package com.norconex.crawler.core.store.impl.ksqldb;

import java.util.Optional;

public class KsqldbDataStoreTest {
    private static final String KSQL_SERVER_HOST = "localhost";
    private static final String TABLE_NAME = "DOCS";
    private static final int KSQL_SERVER_PORT = 8088;
    private static final int PARTITIONS = 2;


    public static void main(String[] args) throws InterruptedException {
        Class<TestObject> type = TestObject.class;

        KsqldbDataStore<TestObject> dataStore = new KsqldbDataStore<>(
                null, KSQL_SERVER_HOST, KSQL_SERVER_PORT, TABLE_NAME, type, PARTITIONS
        );

        TestObject doc1 = new TestObject("1", 1, true);
        TestObject doc2 = new TestObject("2", 2, true);
        TestObject doc3 = new TestObject("3", 3, false);

        dataStore.save("1", doc1);
        dataStore.save("2", doc2);
        dataStore.save("3", doc3);

        Optional<TestObject> optional;

        System.out.println("exists: " + dataStore.exists("1"));
        optional = dataStore.find("1");
        System.out.println("find: " + (optional.isPresent() ? optional.get().toJson() : false));
        doc1.setCount(10);
        doc1.setValid(false);
        dataStore.save("1", doc1);

//        Thread.sleep(3000);

        optional = dataStore.findFirst();
        System.out.println("findFirst: " + (optional.isPresent() ? optional.get().toJson() : false));
        System.out.println("delete:" + dataStore.delete("1"));

//        Thread.sleep(3000);

        System.out.println("exists: " + dataStore.exists("1"));

//        dataStore.close();
    }
}
