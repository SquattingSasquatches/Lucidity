package com.squattingsasquatches.lucidity;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RemoteDBAdapter {

	private Context ctx;
	private Intent dbService;
	private HashMap<String, InternalReceiver> receivers;
	private String serverAddress;
	private int serverPort;

	public RemoteDBAdapter(Context ctx) {
		this.ctx = ctx;

		// initialize our db service
		dbService = new Intent(ctx, PHPService.class);
		receivers = new HashMap<String, InternalReceiver>();

	}

	public void addReceiver(String name, InternalReceiver receiver) {
		receiver.addParam("action", name);
		receivers.put(name, receiver);
	}

	public void execute(String name) {
		Log.i("Executing...", name);
		HashMap<String, String> params = receivers.get(name).getParams();
		for (HashMap.Entry<String, String> entry : params.entrySet()) {
			Log.i(entry.getKey(), entry.getValue());
		}
		dbService.putExtra("serverAddress", serverAddress);
		dbService.putExtra("serverPort", serverPort);
		dbService.putExtra("params", receivers.get(name).getParams());
		dbService.putExtra("receiver", receivers.get(name));
		ctx.startService(dbService);
	}

	public boolean stopService() {
		return ctx.stopService(dbService);
	}

	public void unregisterAllReceivers() {
		try {
			receivers.clear();
		} catch (IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}

	public void unregisterReceiver(String name) {
		try {
			receivers.remove(name);
		} catch (IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public Intent getDbService() {
		return dbService;
	}

	public void setDbService(Intent dbService) {
		this.dbService = dbService;
	}

	public HashMap<String, InternalReceiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(HashMap<String, InternalReceiver> receivers) {
		this.receivers = receivers;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
