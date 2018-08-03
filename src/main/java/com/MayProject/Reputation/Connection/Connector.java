package com.MayProject.Reputation.Connection;

import java.net.HttpURLConnection;
import java.net.URL;

import org.w3c.dom.Document;

import com.MayProject.Reputation.Utils.XML;

class Connector {
	private String siteURL;

	Connector(String url) {
		siteURL = url;
	}

	public Document Request(RequestMethod method, String[] params) throws Exception {
		String data = XML.getXMLRequest(method, params);

		URL url = new URL(siteURL);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.getOutputStream().write(data.getBytes());

			return XML.parseDocument(urlConnection.getInputStream());

		} finally {
			urlConnection.disconnect();
		}
	}

}
