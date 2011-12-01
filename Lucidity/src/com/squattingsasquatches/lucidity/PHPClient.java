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
		
        maybeCreateHttpClient();
        StringBuilder strb = new StringBuilder();
        String uri = "http://" + Config.SERVER_ADDRESS  + "/" + params.get("action") + ".php";
        strb.append(uri);
        
        
        params.remove("action");
        strb.append("?");
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            strb.append(entry.getKey());
            strb.append("=");
            strb.append(entry.getValue());
        }
        
        Log.i("PHPClient", strb.toString() );

        HttpGet get = new HttpGet( strb.toString() );
        HttpProtocolParams.setUserAgent(httpClient.getParams(), "android");
        
        HttpResponse resp;
        
			try {
				resp = httpClient.execute( get );
				DataInputStream is = new DataInputStream( resp.getEntity().getContent() );
				String tmp = is.readLine();
				is.close();
				if( tmp == "" )
					return new JSONArray();
	        	return new JSONArray( tmp );
			} catch (ClientProtocolException e) {
				Log.i("PHPClient","CLientProtocol Error");
				e.getMessage();
			} catch (IOException e) {
				Log.i("PHPClient","IO Error");
				Log.i("PHPClient","Exception: " + e.getMessage());
				e.getMessage();
				
			} catch (JSONException e) {
				Log.i("PHPClient","JSON Error");
				e.getMessage();
				//e.printStackTrace();
			} catch(Exception e) {
				Log.i("PHPClient","Exception: " + e.getMessage());
				e.printStackTrace();
			}
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
