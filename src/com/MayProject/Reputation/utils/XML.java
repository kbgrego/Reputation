package com.MayProject.Reputation.Utils;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.MayProject.Reputation.Connection.RequestMethod;

public class XML {
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public static final void prettyPrint(Document xml) throws Exception {

		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.print(out.toString());

	}

	public static final String getPlainString(Document xml) throws Exception {

		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "no");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		return out.toString();

	}

	public static String getXMLRequest(RequestMethod method, String[] params) throws Exception {

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();

		Element root = doc.createElement("methodCall");
		doc.appendChild(root);

		Element name = doc.createElement("methodName");
		name.setTextContent(method.toStirng());
		root.appendChild(name);

		Element eParams = doc.createElement("params");
		for (String param : params) {
			Element eParam = doc.createElement("param");
			Element eValue = doc.createElement("value");

			eValue.setTextContent(param);
			eParam.appendChild(eValue);
			eParams.appendChild(eParam);
		}
		root.appendChild(eParams);		
		return getPlainString(doc);

	}

	public static Document parseDocument(InputStream is) throws Exception {

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(is);
		
		return doc;

	}

	public static String getValue(Document xml, String name) {
		NodeList list = xml.getElementsByTagName("member");
		
		for( int i=0;i<list.getLength();i++ ) {
			Node found = getNode(list.item(i),"name");
			if(found!=null && found.getTextContent().equals(name))
				return getNode(list.item(i),"value").getTextContent();				
		}
		
		return "";
	}

	private static Node getNode(Node item, String string) {
		NodeList list = item.getChildNodes();
		for(int i=0;i<item.getChildNodes().getLength();i++)
			if(list.item(i).getNodeName().equals(string))
				return list.item(i);
		return null;
	}
	
	
}
