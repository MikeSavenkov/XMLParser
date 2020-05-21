import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class XMLParse {

    InputStream addRootTagInXml() throws FileNotFoundException {
        InputStream file = new FileInputStream("test.xml");
        Scanner s = new Scanner(file).useDelimiter("\\A");
        String xmlString = s.hasNext() ? s.next() : "";

        StringBuilder newXmlString = new StringBuilder(xmlString);
        newXmlString.insert(38, "<root>").append("</root>");
        String str = new String(newXmlString);

        return new ByteArrayInputStream(str.getBytes());
    }

    int getValuesFromXml(List<Integer> list, Document fileNameXML) throws DOMException, XPathExpressionException {

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("root/main/items/item[@exclude='true']");
        XPathExpression expr2 = xpath.compile("root/advanced/items/item[@exclude='true']");
        NodeList nodes = (NodeList) expr.evaluate(fileNameXML, XPathConstants.NODESET);
        NodeList nodes2 = (NodeList) expr2.evaluate(fileNameXML, XPathConstants.NODESET);
        Node n;
        for (int i = 0; i < nodes.getLength(); i++) {
            n = nodes.item(i);
            list.add(Integer.valueOf(n.getTextContent()));
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            n = nodes2.item(i);
            list.add(Integer.valueOf(n.getTextContent()));
        }
        int rubles = 0;
        for (int i : list) {
            rubles += i;
        }
        return rubles;
    }

}
