package com;


import com.lbeen.spring.common.util.R;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {
    public static void main(String[] args) throws Exception {
//        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
//        options.cursorFinalizerEnabled(true);
//        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
//        options.connectTimeout(30000);// 连接超时，推荐>3000毫秒
//        options.maxWaitTime(5000); //
//        options.socketTimeout(0);// 套接字超时时间，0无限制
//        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
////        options.writeConcern(WriteConcern.JOURNALED);//
//        ServerAddress serverAddress = new ServerAddress("192.168.0.151", 27017);
//        MongoDatabase mydb = new MongoClient(serverAddress, options.build()).getDatabase("mydb");
//
//        MongoCollection<Document> test = mydb.getCollection("test1");


//        Document document = new Document();
//        document.put("_id", R.uuid());
//        document.put("name", "name");
//        test.insertOne(document);


//        long count = test.countDocuments();
//        System.out.println(count);
//
//        DBCursor

//        FindIterable<Document> documents = test.find();
//
//        for (Document document : documents) {
//            System.out.println(document);
//        }


    }
}
