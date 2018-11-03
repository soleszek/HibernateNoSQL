package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import pl.sdacademy.mongo.utils.MongoUtils;

import java.util.UUID;

public class DataOperations {

    static MongoClient client = MongoUtils.getInstance().getClient();
    static MongoDatabase database = client.getDatabase("testy_nasze");
    static MongoCollection<Document> collection = database.getCollection("wartości");

    public static void main(String[] args) {
        insertData();
        printData(10);
    }

    private static void insertData() {

        for (int i = 0; i < 100; i++) {
            //Document musi być org.bson
            Document doc = new Document("text", UUID.randomUUID().toString())
                    .append("value", (int)Math.random() * 1000);
            collection.insertOne(doc);
        }
    }

    private static void printData(int limit) {
        MongoIterable<Document> results = collection.find().limit(limit);
        for(Document doc : results) {
            System.out.println(doc.toJson());
        }
    }
}
