package com.MayProject.Reputation.Connection;

import org.w3c.dom.Document;

import com.MayProject.Reputation.utils.XML;

class SiteProcessing {
	/**
	 * 
	 * @return token
	 */
	static String processAuth(Document xml) throws Exception {

		if (processCheckAuth(xml))
			return XML.getValue(xml,"token");

		return "";
	}

	/**
	 * 
	 * @return success of authorization with token
	 */
	public static boolean processCheckAuth(Document xml) {
		
		return XML.getValue(xml,"auth").equals("1");

	}
}
