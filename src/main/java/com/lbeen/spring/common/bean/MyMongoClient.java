package com.lbeen.spring.common.bean;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MyMongoClient {
    private MongoClient client;
    private String dbName;

    public MyMongoClient(MongoClient client, String dbName) {
        this.client = client;
        this.dbName = dbName;
    }

    public MongoCollection<Document> getCollection(String collectionName){
        return client.getDatabase(dbName).getCollection(collectionName);
    }
}
