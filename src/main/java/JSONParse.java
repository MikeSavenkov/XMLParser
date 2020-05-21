import com.jayway.jsonpath.JsonPath;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.text.DecimalFormat;

public class JSONParse {

    private static final String url = "https://www.cbr-xml-daily.ru/daily_json.js";

    String UrlJsonToString() throws Exception {

        BufferedReader reader = null;
        try {
            URL url = new URL(JSONParse.url);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    void computeEuro(int rubles) throws Exception {

        String jsonString = UrlJsonToString();
        double euroRate = JsonPath.read(jsonString, "$.Valute.EUR.Value");
        double euro = rubles / euroRate;
        DecimalFormat df = new DecimalFormat("###.###");
        System.out.printf("count € in %d₽: %.3f€", rubles, euro);
        Writer writer = new FileWriter("log.txt");
        writer.write("count € in " + rubles + "₽: " + df.format(euro) + "€");
        writer.close();
    }

}
