package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.sdacademy.mongo.utils.MongoUtils;

import java.io.IOException;

public class Robot {

    static MongoDatabase database = MongoUtils.getInstance().getClient().getDatabase("robot");

    public static void main(String[] args) throws IOException {
        //String host = "https://onet.pl";
        String host = "https://nadzory-archeologiczne.pl";
        Document result = Jsoup.connect(host).followRedirects(true).get();
        //System.out.println(result.outerHtml());

        //Elements urls = result.select("a");

        /*for(Element url : urls) {
            String href = url.absUrl("href");
            System.out.println(href);
        }*/

        searchText("arch");
    }

    private static void insertIfNotExist(Elements urls) {
        MongoCollection<org.bson.Document> urlCollection = database.getCollection("urls");
        for(Element url : urls) {
            String href = url.absUrl("href");
            Iterable<org.bson.Document> found = urlCollection
                            .find(Filters.eq("url", href));
            if(found.iterator().hasNext() == false) {
                org.bson.Document newUrl = new org.bson.Document("url", href)
                        .append("used", false);
                urlCollection.insertOne(newUrl);
            }
        }
    }

    private static void searchText(String text) throws IOException {
        MongoCollection<org.bson.Document> urlCollection = database.getCollection("urls");
        MongoCollection<org.bson.Document> foundCollection = database.getCollection("found");

        org.bson.Document url = urlCollection
                .find(Filters.eq("used", false))
                .first();

        Document result = Jsoup
                //getString - wyciąganie pojedynczego stringa z urla (z adresu)
                .connect(url.getString("url"))
                .followRedirects(true)
                .get();
        Integer position = result.text().indexOf(text);
        if(position > 0) {
            org.bson.Document foundItem = new org.bson.Document("url", url.getString("url"))
                    .append("content", result.text())
                    .append("foundAt", position);
            foundCollection.insertOne(foundItem);
        }

        Object id = url.getObjectId("_id");
        urlCollection
                .updateOne(Filters.eq("_id", id), Updates.set("used", true));

        insertIfNotExist(result.select("a"));
    }

}
