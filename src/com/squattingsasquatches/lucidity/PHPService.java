package com.squattingsasquatches.lucidity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class PHPService extends IntentService {

	public PHPService() {
		super("PHPService");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onHandleIntent(Intent intent) {
		final HashMap<String, String> params = (HashMap<String, String>) intent
				.getSerializableExtra("params");
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final Bundle b = new Bundle();
		InputStream is;
		final String action = params.get("action");
		String result = "", line = "";

		final ArrayList<NameValuePair> nvParams = new ArrayList<NameValuePair>();

		if (action == null) {
			Log.e("query()", "No action specified");
			return;
		}

		for (final HashMap.Entry<String, String> p : params.entrySet())
			if (!p.getKey().equals("action"))
				nvParams.add(new BasicNameValuePair(p.getKey(), p.getValue()));

		Log.i("action", action);

		try {
			final HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					Config.USER_AGENT);
			Log.i("PHPService", intent.getStringExtra("serverAddress"));

			final HttpPost httppost = new HttpPost("http://"
					+ intent.getStringExtra("serverAddress") + ":"
					+ intent.getIntExtra("serverPort", 80) + "/" + action
					+ ".php");
			httppost.setEntity(new UrlEncodedFormEntity(nvParams));

			final HttpResponse response = httpclient.execute(httppost);
			final HttpEntity entity = response.getEntity();

			is = entity.getContent();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			while ((line = reader.readLine()) != null)
				result += line;

			b.putString(Codes.KEY_RESULT, result);
			receiver.send(Codes.REMOTE_QUERY_COMPLETE, b);

			Log.i("result", result);

		} catch (final UnsupportedEncodingException e) {

			Log.e("PHPService", "Unsupported Encoding: " + e.getMessage()
					+ ". action: " + action);

		} catch (final HttpResponseException e) {

			b.putInt("httpstatuscode", e.getStatusCode());
			Log.e("PHPService", "HttpResponseException: " + e.getMessage());
			receiver.send(Codes.REMOTE_QUERY_ERROR, b);

		} catch (final IOException e) {

			b.putString("connection_error_code", e.getMessage());
			Log.e("PHPService", "IOException: " + e.getMessage());
			receiver.send(Codes.REMOTE_CONNECTION_ERROR, b);

		} catch (final Exception e) {

			Log.e("PHPService", "Exception: " + e.getMessage());
			receiver.send(Codes.REMOTE_QUERY_ERROR, b);

		}

		this.stopSelf();

	}
}