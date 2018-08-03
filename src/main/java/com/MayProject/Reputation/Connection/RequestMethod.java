package com.MayProject.Reputation.Connection;

public enum RequestMethod {
	AUTH("getAuth"), 
	CHECKAUTH("checkAuth");

	public static final String PREFIX = "reputation";

	private final String Method;

	RequestMethod(String method) {
		Method = PREFIX + "." + method;
	}

	public String toStirng() {
		return Method;
	}

}
