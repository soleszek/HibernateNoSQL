package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import pl.sdacademy.mongo.utils.MongoUtils;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DataOperations {

    static MongoClient client = MongoUtils.getInstance().getClient();
    static MongoDatabase database = client.getDatabase("testy_nasze");
    static MongoCollection<Document> collection = database.getCollection("wartości");

    public static void main(String[] args) throws InterruptedException {
        //insertData();
        //printData(10);
        //findData("1d506545-f9e9-4711-b52c-9645dbe767b7");
        //findData(10, 10000);
        //findDataById("5bdd645df9f04849b4c771fd");
        //update("5bdd645df9f04849b4c771fd", "mój tekst do aktualizacji");
        //findDataById("5bdd645df9f04849b4c771fd");

        //findData(11, 12);
        //update(11, 12, "nowy tekst");
        //findData(11, 12);

        //findData(11, 12);
        //delete(11, 12);
        //findData(11, 12);

        //findDataById("5bdd6eb1f9f0484c94746425");
        //deleteById("5bdd6eb1f9f0484c94746425");
        //findDataById("5bdd6eb1f9f0484c94746425");

        //insertDataWithDataAndRandom();

        while(true) {
            insertDataWithDataAndRandom();
            Thread.sleep(1000);
            printData(100);
        }
    }

    private static void insertData() {

        for (int i = 0; i < 100000; i++) {
            //Document musi być org.bson
            Document doc = new Document("text", UUID.randomUUID().toString())
                    .append("value", (int) (Math.random() * 1000));
            collection.insertOne(doc);
        }
    }

    private static void insertDataWithDataAndRandom() {

        for (int i = 0; i < 2; i++) {

            ArrayList<Integer> list = new ArrayList<Integer>();
            for(int k=0; k < (int) (Math.random() * 10); k++) {
                list.add((int) (Math.random() * 1000));
            }
            //Document musi być org.bson
            Document doc = new Document("text", UUID.randomUUID().toString())
                    .append("value", (int) (Math.random() * 1000))
                    .append("date", new Date())
                    .append("random_table", list)
                    .append("test", "dane me")
                    .append("name", "Sylwester");
            collection.insertOne(doc);
        }
    }

    private static void printData(int limit) {
        MongoIterable<Document> results = collection.find().limit(limit);
        for (Document doc : results) {
            System.out.println(doc.toJson());
        }
    }

    private static void findData(String text) {
        System.out.println("Found: ");
        MongoIterable<Document> results = collection.find(Filters.eq("text", text));
        for (Document doc : results) {
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
        for (Document doc : results) {
            System.out.println(doc.toJson());
        }
    }

    private static void findDataById(String id) {
        Document result = collection
                .find(Filters.eq("_id", new ObjectId(id)))
                .first();
        if (result != null) {
            System.out.println(result.toJson());
        }
    }

    private static void update(String id, String text) {
        System.out.println("Update id=" + id + " with value=" + text);
        collection
                .updateOne(
                        Filters.eq("_id", new ObjectId(id)),
                        Updates.set("text", text)
                );
    }

    private static void update(Integer rangeMin, Integer rangeMax, String text) {
        System.out.println("Updated in range (" + rangeMin + ", " + rangeMax + ")" + "with data " + text);
        collection
                .updateMany(
                Filters.and(
                        //greater than equal
                        Filters.gte("value", rangeMin),
                        //less than equal
                        Filters.lte("value", rangeMax)),
                Updates.set("text", text)
                );
    }

    private static void deleteById(String id) {

        System.out.println("Deleted id=" + id);
        collection
                .deleteOne(
                        Filters.eq("_id", new ObjectId("5bd1c35a1e657c8611942ec6"))
                );
    }

    private static void delete(Integer rangeMin, Integer rangeMax) {

        System.out.println("Deleted in range (" + rangeMin + ", " + rangeMax + ")");
        collection
                .deleteMany(
                        Filters.and(
                                Filters.gte("value", rangeMin),
                                Filters.lte("value", rangeMax)));
    }
}
