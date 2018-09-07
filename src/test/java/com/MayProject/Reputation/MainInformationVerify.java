package com.MayProject.Reputation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class MainInformationVerify {
	
	@Test
	void VersionVerify() {
		System.out.println("VersionVerify");
		assertTrue(Core.getVersion().matches("V\\s\\d.\\d.\\d(\\s[A-Z]+)*"), "Fail in > " + Core.getVersion());
	}
	
	@Test
	void CopyrightVerify() throws NoSuchAlgorithmException {
		System.out.println("CopyrightVerify");
		byte[] md5 = MessageDigest.getInstance("MD5").digest(Core.getCopyright().getBytes());		
		assertEquals("3026638F4DC90856512A1599308FEE28", bytesToHex(md5));
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
