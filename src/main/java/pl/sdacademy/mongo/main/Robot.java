package pl.sdacademy.mongo.main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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

        Elements urls = result.select("a");

        /*for(Element url : urls) {
            String href = url.absUrl("href");
            System.out.println(href);
        }*/

        insertIfNotExist(urls);
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
}
