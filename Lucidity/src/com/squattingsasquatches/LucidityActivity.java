package com.squattingsasquatches;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

public class LucidityActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView textAndroidId = (TextView)findViewById(R.id.uuid);
        String AndroidId = Settings.Secure.getString(getContentResolver(),
          Settings.Secure.ANDROID_ID);
        textAndroidId.setText("My ID is: " + AndroidId);
    }
}