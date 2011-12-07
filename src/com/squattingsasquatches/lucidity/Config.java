package com.squattingsasquatches.lucidity;

public final class Config {
	public static String SERVER_ADDRESS = "http://130.160.211.88:8080";
	public static final String USER_AGENT = "Lucidity/android";
	
	public static void setServerAddress(String ADDRESS){
		SERVER_ADDRESS = ADDRESS;
	}
}