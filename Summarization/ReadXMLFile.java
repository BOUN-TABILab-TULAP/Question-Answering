import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

public class ReadXMLFile {
    public static ArrayList<String> titles2 = new ArrayList<String>();
    public static ArrayList<String> sub = new ArrayList<String>();
    public static ArrayList<String> sub2 = new ArrayList<String>();
    public static ArrayList<String> sub3 = new ArrayList<String>();
	
    // public static void main(String argv[]) {
 
    //  try {
    public static void ReadXML(){
	try{
	    File fXmlFile = new File("new.xml");
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);
 
	    //optional, but recommended
	    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	    doc.getDocumentElement().normalize();
 
	    //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
	    NodeList nList = doc.getElementsByTagName("content");
 
	    //System.out.println("----------------------------");
 
	    for (int temp = 0; temp < nList.getLength(); temp++) {
 
		Node nNode = nList.item(temp);
 
		// System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
		    Element eElement = (Element) nNode;
	
		    /*
		      System.out.println("Content id : " + eElement.getAttribute("id"));
		      System.out.println("baþlýk: " + eElement.getElementsByTagName("title").item(0).getTextContent());
		      System.out.println("alt baþlýk : " + eElement.getElementsByTagName("sub").item(0).getTextContent());
		      System.out.println("alt baþlýk : " + eElement.getElementsByTagName("sub2").item(0).getTextContent());
		      System.out.println("alt baþlýk : " + eElement.getElementsByTagName("sub3").item(0).getTextContent());
		    */
			
		    titles2.add(eElement.getElementsByTagName("title").item(0).getTextContent());
		    sub.add (eElement.getElementsByTagName("sub").item(0).getTextContent());
		    sub2.add (eElement.getElementsByTagName("sub2").item(0).getTextContent());
		    sub3.add (eElement.getElementsByTagName("sub3").item(0).getTextContent());     
		}
	    }
	    //	textproc.getXML(titles2,sub,sub2,sub3);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
 
}
