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
		this.dbService = new Intent(ctx, PHPService.class);
		this.receivers = new HashMap<String, InternalReceiver>();

	}

	public void addReceiver(String name, InternalReceiver receiver) {
		receiver.addParam("action", name);
		this.receivers.put(name, receiver);
	}

	public void execute(String name) {
		Log.i("Executing...", name);
		final HashMap<String, String> params = this.receivers.get(name)
				.getParams();
		for (final HashMap.Entry<String, String> entry : params.entrySet())
			Log.i(entry.getKey(), entry.getValue());
		this.dbService.putExtra("serverAddress", this.serverAddress);
		this.dbService.putExtra("serverPort", this.serverPort);
		this.dbService.putExtra("params", this.receivers.get(name).getParams());
		this.dbService.putExtra("receiver", this.receivers.get(name));
		this.ctx.startService(this.dbService);
	}

	public Context getCtx() {
		return this.ctx;
	}

	public Intent getDbService() {
		return this.dbService;
	}

	public HashMap<String, InternalReceiver> getReceivers() {
		return this.receivers;
	}

	public String getServerAddress() {
		return this.serverAddress;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}

	public void setDbService(Intent dbService) {
		this.dbService = dbService;
	}

	public void setReceivers(HashMap<String, InternalReceiver> receivers) {
		this.receivers = receivers;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public boolean stopService() {
		return this.ctx.stopService(this.dbService);
	}

	public void unregisterAllReceivers() {
		try {
			this.receivers.clear();
		} catch (final IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}

	public void unregisterReceiver(String name) {
		try {
			this.receivers.remove(name);
		} catch (final IllegalArgumentException e) {
			Log.i("unregisterReceiver", "receiver not registered");
		}
	}

}
