package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import pl.sdacademy.mongo.utils.MongoUtils;

import javax.print.Doc;
import java.util.UUID;

public class DataOperations {

    static MongoClient client = MongoUtils.getInstance().getClient();
    static MongoDatabase database = client.getDatabase("testy_nasze");
    static MongoCollection<Document> collection = database.getCollection("wartości");

    public static void main(String[] args) {
        insertData();
        printData(10);
        //findData("1d506545-f9e9-4711-b52c-9645dbe767b7");
        findData(10, 10000);
    }

    private static void insertData() {

        for (int i = 0; i < 100000; i++) {
            //Document musi być org.bson
            Document doc = new Document("text", UUID.randomUUID().toString())
                    .append("value", (int)(Math.random() * 1000));
            collection.insertOne(doc);
        }
    }

    private static void printData(int limit) {
        MongoIterable<Document> results = collection.find().limit(limit);
        for(Document doc : results) {
            System.out.println(doc.toJson());
        }
    }

    private static void findData(String text){
        System.out.println("Found: ");
        MongoIterable<Document> results = collection.find(Filters.eq("text", text));
        for(Document doc : results) {
            System.out.println(doc.toJson());
        }

    }

    private static void findData(Integer rangeMin, Integer rangeMax) {
        System.out.println("Found in range (" + rangeMin + ", " + rangeMax + ")");
        MongoIterable<Document> results = collection
                .find(Filters.and(
                        //greater than equal
                        Filters.gte("value", rangeMin),
                        //less than equal
                        Filters.lte("value", rangeMax)
                ));
        for(Document doc : results) {
            System.out.println(doc.toJson());
        }
    }
}
