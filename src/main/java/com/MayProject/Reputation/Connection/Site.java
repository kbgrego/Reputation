package com.MayProject.Reputation.Connection;

public class Site {
	private static final Connector CONNECTOR = new Connector("http://may-project.pp.ua/xmlrpc.php");

	private static String User;
	private static String Password;
	private static String Token;

	public static boolean Auth() throws Exception {
		Token = SiteProcessing.processAuth(CONNECTOR.Request(RequestMethod.AUTH, getCredentials()));		
		return !Token.isEmpty();
	}

	public static boolean CheckAuth() throws Exception {
		if(!Token.isEmpty())
			return SiteProcessing.processCheckAuth(CONNECTOR.Request(RequestMethod.CHECKAUTH, getToken()));
		return false;
	}
	
	public static String RegistrateAuth(String form) throws Exception {		
		return SiteProcessing.processCheckReg(CONNECTOR.Request(RequestMethod.REGISTRATEAUTH, getForm(form)));
	}

	public static String ReceiveMessage() throws Exception {
		return SiteProcessing.processReceiveMessage(CONNECTOR.Request(RequestMethod.RECEIVEMESSAGE, getToken()));
	}

	private static String[] getToken() {
		return new String[] { Token };
	}

	private static String[] getCredentials() {
		return new String[] { User, Password };
	}

	private static String[] getForm(String form) {
		return new String[] { User, Password, form };
	}

	public static String getUser() {
		return User;
	}

	public static void setUser(String user) {
		User = user;
	}

	public static String getPassword() {
		return Password;
	}

	public static void setPassword(String password) {
		Password = password;
	}

	public static void clear() {
		User = "";
		Password = "";
		Token = "";
	}

}
