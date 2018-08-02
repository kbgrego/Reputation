package com.MayProject.Reputation.Connection;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

class SiteProcessing {
	/**
	 * 
	 * @return token
	 */
	static String processAuth(Document xml) throws Exception {
		boolean auth = false;

		NodeList list = xml.getElementsByTagName("boolean");
		for (int i = 0; i < list.getLength(); i++)
			auth = list.item(i).getTextContent().equals("1");

		if (auth)
			return xml.getElementsByTagName("string").item(0).getTextContent().split("\\|")[2];

		throw new Exception("no authorization");
	}

	/**
	 * 
	 * @return success of authorization with token
	 */
	public static boolean processCheckAuth(Document xml) {
		boolean auth = false;

		NodeList list = xml.getElementsByTagName("boolean");
		for (int i = 0; i < list.getLength(); i++)
			auth = list.item(i).getTextContent().equals("1");

		return auth;
	}
}
