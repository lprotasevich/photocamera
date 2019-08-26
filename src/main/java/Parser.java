import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "https://xn--90adear.xn--p1ai/milestones";
        Document page = Jsoup.connect(url).maxBodySize(0).timeout(0).get();
        return page;
    }

    public static void main(String[] args) throws IOException{

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("photocamera");
        MongoCollection collection = database.getCollection("adresses");
        collection.deleteMany(new org.bson.Document());

        Document page = getPage();
        Element p = page.select("div[id=list]").first();

        Element script = p.selectFirst("script");
        String[] parts = script.toString().split("coordinates: ");
        String[] resultParts = new String[2795];

        int j = 1;
        int k = 0;
        myMap[] massMap = new myMap[2795];
        for(String respart : resultParts){
            respart = parts[j].substring(parts[j].indexOf('[') + 1, parts[j++].indexOf(']'));
            massMap[k++] = new myMap(respart);

            System.out.println(respart);
            System.out.println("*********************");
        }





        //System.out.println(script);
        //css query language
        Elements textAdresses = page.select("div.sl-item-text");
        Elements adressLinks = p.select("a[href]");

        ArrayList<org.bson.Document> list = new ArrayList();

        for(Element adress : textAdresses)
            list.add(new org.bson.Document("adress",adress.text()));

        int i = 0;
        for(Element link : adressLinks)
            list.get(i).append("link", "https://xn--90adear.xn--p1ai" + link.attr("href")).append("latitude", massMap[i].getLatitude()).append("longitude", massMap[i++].getLongitude());

        collection.insertMany(list);



    }
}
