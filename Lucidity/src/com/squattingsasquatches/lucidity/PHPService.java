package com.squattingsasquatches.lucidity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
    	HashMap<String, String> params = (HashMap<String, String>) intent.getSerializableExtra("params");
    	
    	final ResultReceiver receiver = intent.getParcelableExtra("receiver");
    	Bundle b = new Bundle();
    	
    	InputStream is;
    	String action = params.get("action"),
    			result = "",
    			line = "";
        
    	ArrayList<NameValuePair> nvParams = new ArrayList<NameValuePair>();
				
		if (action == null) {
			Log.e("query()", "No action specified");
			return;
		}
		
		for (HashMap.Entry<String, String> p : params.entrySet()) {
			if (!p.getKey().equals("action"))
				nvParams.add(new BasicNameValuePair(p.getKey(), p.getValue()));
		}
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("https://www.thesouthernshirtco.com/lucidity/" + action + ".php");
			httppost.setEntity(new UrlEncodedFormEntity(nvParams));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				
				while ((line = reader.readLine()) != null) {
					result += line;
				}
				
				Log.i("result", result);
				
				b.putString(Codes.KEY_RESULT, result);
				b.putInt(Codes.KEY_CALLBACK, intent.getIntExtra(Codes.KEY_CALLBACK, Codes.NO_CALLBACK));
				
                receiver.send(Codes.REMOTE_QUERY_COMPLETE, b);
				
			} catch (UnsupportedEncodingException e) {
				Log.e("DBService", "Error getting http result " + e.getMessage());
			}
			
		} catch (Exception e) {
			// No internet connection
			/* TODO: set result as something to notify LucidityActivity that there is no connection
			 * App can still function, just at a very basic, barebones level.
			 */
			Log.e("DBService", "Error in http connection " + e.getMessage());
			b.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Codes.REMOTE_QUERY_ERROR, b);
		}
		
		/*// send result back to main activity
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(RemoteDBAdapter.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(PARAM_OUT_MSG, result);
        sendBroadcast(broadcastIntent);*/
		this.stopSelf();
    }
}
