package pl.sdacademy.mongo.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoUtils {
    private final static MongoUtils instance = new MongoUtils();
    private MongoClient client;

    //MongoClients - fabryka obiektów
    private MongoUtils() {
        client = MongoClients.create("mongodb://localhost");
    }

    public static MongoUtils getInstance() {
        return instance;
    }

    public MongoClient getClient() {
        return client;
    }
}
