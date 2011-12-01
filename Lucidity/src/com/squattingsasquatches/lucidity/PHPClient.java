package com.squattingsasquatches.lucidity;

import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;    
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.*;

import android.util.Log;

import java.io.DataInputStream;
import java.util.HashMap;	
import java.io.IOException;

public class PHPClient {
	DefaultHttpClient httpClient;
	int timeOut = 30 * 1000;
	public JSONArray execute( HashMap<String, String> params )
	{
//		Looper.prepare();
//		Thread t = new Thread(){
//			public void run() 
//			{
//				
//			}
//		};
//		t.start();
		
        maybeCreateHttpClient();
        HttpGet get = new HttpGet( "https://www.thesouthernshirtco.com/lucidity/" + params.get("action") + ".php" );

        HttpProtocolParams.setUserAgent(httpClient.getParams(), "android");
        
        params.remove("action");
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            get.getParams().setParameter(entry.getKey(), entry.getValue());
        }
        Log.i( "execute", (String)get.getParams().getParameter("uni_id") );
		Log.i("execute", "Line 47");
        	HttpResponse resp;
			try {
				resp = httpClient.execute( get );
				DataInputStream is = new DataInputStream( resp.getEntity().getContent() );
				String tmp = is.readLine();
				Log.i("PHPClient",tmp);
	        	return new JSONArray( tmp );
			} catch (ClientProtocolException e) {
				Log.i("PHPClient","CLientProtocol Error");
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("PHPClient","IO Error");
				e.printStackTrace();
			} catch (JSONException e) {
				Log.i("PHPClient","JSON Error");
				e.printStackTrace();
			}
			Log.i("OnCreate", "Line 63");
        	return new JSONArray();
	}
	private void maybeCreateHttpClient() {
        if ( httpClient == null) {
            httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeOut);
            HttpConnectionParams.setSoTimeout(params, timeOut);
            ConnManagerParams.setTimeout(params, timeOut);
        }
    }

}
