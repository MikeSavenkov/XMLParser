import com.jayway.jsonpath.JsonPath;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String urlString = "https://www.cbr-xml-daily.ru/daily_json.js";
        InputStream file = new FileInputStream("test.xml");
        Scanner s = new Scanner(file).useDelimiter("\\A");
        String xmlString = s.hasNext() ? s.next() : "";


        StringBuilder newXmlString = new StringBuilder(xmlString);
        newXmlString.insert(38, "<root>").append("</root>");
        String str = new String(newXmlString);

        InputStream targetStream = new ByteArrayInputStream(str.getBytes());

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(targetStream);
            List<Integer> list = new ArrayList<>();

            int count = parser(list, document);
            readUrl(urlString);
            json(count);

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            ex.printStackTrace(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static int parser(List<Integer> list, Document fileNameXML) throws DOMException, XPathExpressionException {

//        System.out.println("Example 1 - Печать всех элементов Cost");
//        XPathFactory pathFactory = XPathFactory.newInstance();
//        XPath xpath = pathFactory.newXPath();

        // Пример записи XPath
        // Подный путь до элемента
        //XPathExpression expr = xpath.compile("BookCatalogue/Book/Cost");
        // Все элементы с таким именем
        //XPathExpression expr = xpath.compile("//Cost");
        // Элементы, вложенные в другой элемент
//        XPathExpression expr = xpath.compile("//Book/Cost");
//
//        NodeList nodes = (NodeList) expr.evaluate(fileNameXML, XPathConstants.NODESET);
//        for (int i = 0; i < nodes.getLength(); i++) {
//            Node n = nodes.item(i);
//            System.out.println("Value:" + n.getTextContent());
//        }
//        System.out.println();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("root/main/items/item[@exclude='true']");
        XPathExpression expr2 = xpath.compile("root/advanced/items/item[@exclude='true']");
        NodeList nodes = (NodeList) expr.evaluate(fileNameXML, XPathConstants.NODESET);
        NodeList nodes2 = (NodeList) expr2.evaluate(fileNameXML, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            list.add(Integer.valueOf(n.getTextContent()));
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes2.item(i);
            list.add(Integer.valueOf(n.getTextContent()));
        }
        int count = 0;
        for (int i : list) {
            count += i;
        }
        System.out.println(list.toString());
        System.out.println(count);
        return count;
    }

    private static String readUrl(String urlString) throws Exception {

        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            System.out.println(buffer.toString());
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    static void json(int rubles) throws Exception {

        String jsonString = readUrl("https://www.cbr-xml-daily.ru/daily_json.js");
        double euroRate = JsonPath.read(jsonString, "$.Valute.EUR.Value");
        double euro = rubles / euroRate;
        System.out.println("" + euro);
    }

}
