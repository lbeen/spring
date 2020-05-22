package com.lbeen.spring.common.util;

import com.lbeen.spring.common.bean.MyMongoClient;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;
import com.lbeen.spring.database.service.DatabaseService;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoUtil {
    private static Map<String, MyMongoClient> CLIENTS = new HashMap<>();
    private static Map<String, String> TABLES = new HashMap<>();

    static {
        loadCache();
    }

    public static void loadCache() {
        System.out.println("===============MongoDBUtil初始化========================");
        DatabaseService databaseService = SpringUtil.getBean("databaseServiceImpl");

        Map<String, MyMongoClient> clients = new HashMap<>();
        List<Database> databases = databaseService.getUsedMongoDbs();
        if (!CollectionUtils.isEmpty(databases)) {
            MongoClientOptions.Builder options = new MongoClientOptions.Builder();
            options.cursorFinalizerEnabled(true);
            options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
            options.connectTimeout(30000);// 连接超时，推荐>3000毫秒
            options.maxWaitTime(5000); //
            options.socketTimeout(0);// 套接字超时时间，0无限制
            options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
//            options.writeConcern(WriteConcern.JOURNALED);//

            Map<String, MongoClient> mongoClients = new HashMap<>();
            for (Database database : databases) {
                String key = database.getIp() + database.getPort();
                MongoClient client = mongoClients.get(key);
                if (client == null) {
                    ServerAddress serverAddress = new ServerAddress(database.getIp(), Integer.parseInt(database.getPort()));
                    client = new MongoClient(serverAddress, options.build());
                    mongoClients.put(key, client);
                }
                clients.put(database.getId(), new MyMongoClient(client, database.getDbName()));
            }
        }

        Map<String, String> tables = new HashMap<>();
        List<Table> mongoTables = databaseService.getUsedMongoTables();
        if (!CollectionUtils.isEmpty(mongoTables)) {
            for (Table table : mongoTables) {
                tables.put(table.getTableName(), table.getDbId());
            }
        }

        CLIENTS = clients;
        TABLES = tables;
    }

    private static MongoCollection<Document> getMongoCollection(String collectionName) {
        return CLIENTS.get(TABLES.get(collectionName)).getCollection(collectionName);
    }

    public static Long count(String collectionName, Bson filter) {
        return  getMongoCollection(collectionName).countDocuments(filter);
    }

    public static Document findOne(String collectionName, Bson filter) {
        FindIterable<Document> iterable =  getMongoCollection(collectionName).find(filter);
        for (Document document : iterable) {
            return document;
        }
        return null;
    }

    public static List<Document> find(String collectionName, Bson filter) {
        return find(collectionName, filter, null, null);
    }

    public static List<Document> find(String collectionName, Bson filter, Integer skip, Integer limit) {
        ArrayList<Document> result = new ArrayList<>();

        FindIterable<Document> iterable =  getMongoCollection(collectionName).find(filter);
        if (skip != null) {
            iterable.skip(skip);
        }
        if (skip != null) {
            iterable.limit(limit);
        }
        for (Document document : iterable) {
            result.add(document);
        }
        return result;
    }

    public static void delete(String collectionName, Bson filter) {
        getMongoCollection(collectionName).deleteMany(filter);
    }

    public static void insert(String collectionName, Document document) {
        getMongoCollection(collectionName).insertOne(document);
    }

    public static void insertList(String collectionName, List<Document> documents) {
        getMongoCollection(collectionName).insertMany(documents);
    }
}
