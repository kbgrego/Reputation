package com.MayProject.Reputation.Connection;

import org.w3c.dom.Document;

import com.MayProject.Reputation.Utils.XML;

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

	public static String processCheckReg(Document xml) {
		boolean confirmed = XML.getValue(xml,"auth").equals("1");
		String result = "";
		if(!confirmed) {
			result = XML.getValue(xml, "ErrorMessage");
			if(result.isEmpty())
			result = "Failed, try again.";
			
		} else 
			result = "Confirmed";
			
		return result;
	}

	public static String processReceiveMessage(Document xml) {
		return XML.getValue(xml, "message");
	}
}
