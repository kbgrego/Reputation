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

	private static String[] getToken() {
		return new String[] { Token };
	}

	private static String[] getCredentials() {
		return new String[] { User, Password };
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

}
