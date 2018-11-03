package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoIterable;
import pl.sdacademy.mongo.utils.MongoUtils;

public class Main {
    public static void main(String[] args) {
        MongoClient client = MongoUtils.getInstance().getClient();
        MongoIterable<String> databases = client.listDatabaseNames();
        for (String database : databases) {
            System.out.println(database);
        }
    }
}
