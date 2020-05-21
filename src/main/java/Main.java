import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        XMLParse xmlParse = new XMLParse();
        JSONParse jsonParse = new JSONParse();

        InputStream targetStream = xmlParse.addRootTagInXml();
        List<Integer> list = new ArrayList<>();

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(targetStream);

            int rubles = xmlParse.getValuesFromXml(list, document);
            jsonParse.UrlJsonToString();
            jsonParse.computeEuro(rubles);

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            ex.printStackTrace(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
