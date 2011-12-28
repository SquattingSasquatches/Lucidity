package com.squattingsasquatches.lucidity;

public final class Config {
	public static final String DB_NAME = "lucidity";
	public static String SERVER_ADDRESS = "http://192.168.1.4";

	public static final String USER_AGENT = "Lucidity/android";

	public static void setServerAddress(String ADDRESS) {
		SERVER_ADDRESS = ADDRESS;
	}
}