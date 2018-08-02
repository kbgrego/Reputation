package com.MayProject.Reputation.Connection;

public class Site {
	private static final Connector CONNECTOR = new Connector("http://may-project.pp.ua/xmlrpc.php");

	private static String User = "RepTest";
	private static String Password = "153246879001524568";
	private static String Token;

	public static void Auth() throws Exception {
		Token = SiteProcessing.processAuth(CONNECTOR.Request(RequestMethod.AUTH, getCredentials()));
		if (!Token.isEmpty())
			System.out.println("Authorized successfuly!");
	}

	public static void CheckAuth() throws Exception {
		boolean success = SiteProcessing.processCheckAuth(CONNECTOR.Request(RequestMethod.CHECKAUTH, getToken()));

		if (success)
			System.out.println("Checked successfuly!");
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
