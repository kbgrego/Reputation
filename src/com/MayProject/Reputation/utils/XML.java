package com.MayProject.Reputation.utils;

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

import com.MayProject.Reputation.Connection.RequestMethod;

public class XML {
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public static final void prettyPrint(Document xml) throws Exception {

		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.println(out.toString());

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
}
