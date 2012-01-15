package com.squattingsasquatches.lucidity.activities;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;

import com.squattingsasquatches.lucidity.AutoCompleteArrayAdapter;
import com.squattingsasquatches.lucidity.InternalReceiver;
import com.squattingsasquatches.lucidity.R;
import com.squattingsasquatches.lucidity.R.id;
import com.squattingsasquatches.lucidity.R.layout;
import com.squattingsasquatches.lucidity.objects.University;


//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MenuItem.OnMenuItemClickListener;
//import android.view.View;
//import android.view.View.OnFocusChangeListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ViewFlipper;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemClickListener;

public class UniversitySelectActivity extends LucidityActivity {

	private University selectedUni;
	private AutoCompleteTextView txtUni;
	private ArrayList<University> unis;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.university_select);

        txtUni = (AutoCompleteTextView) findViewById(R.id.acUni);
        
        remoteDB.addReceiver("unis.view", universitiesView);
        remoteDB.execute("unis.view");
	}
	public void sendResult() {
		Intent in = new Intent();
        in.putExtra("id", selectedUni.getId());
        in.putExtra("name", selectedUni.getName());
        setResult(1,in);
        finish();
	}
	public void loadUniversities(JSONArray result) {
    	AutoCompleteArrayAdapter<University> adapter;
    	unis = new ArrayList<University>();
    	int resultLength = result.length();
    	
    	for (int i = 0; i < resultLength; ++i) {
    		try {
				unis.add(new University(result.getJSONObject(i).getInt("id"), result.getJSONObject(i).getString("name")));
			} catch (JSONException e) {
				Log.d("loadUniversities", "error loading univerisites");
			}
    	}
    	
    	adapter = new AutoCompleteArrayAdapter<University>(this, R.layout.ac_list_item, unis);
    	txtUni.setAdapter(adapter);
    	txtUni.setOnItemClickListener(uniClickListener);
    	txtUni.setValidator(new ACValidator());
    	txtUni.setOnFocusChangeListener(new ValidateStarter());
    	//loading.dismiss();
    	//layoutFlipper.showNext();
    }
	private OnItemClickListener uniClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View selectedView, int position, long id) {
			selectedUni = (University) parentView.getAdapter().getItem(position);
		}
    	
    };
	private InternalReceiver universitiesView = new InternalReceiver(){
		@Override
		public void update( JSONArray data ){
			UniversitySelectActivity.this.loadUniversities( data );
		}
		@Override
		public void onHttpError( int statusCode ){
			//UniversitySelectActivity.this.onConnectionError( statusCode );
		}
		@Override
		public void onConnectionError( String errorMessage ){
			//UniversitySelectActivity.this.onHttpError( errorMessage );
		}
	};
	class ACValidator implements AutoCompleteTextView.Validator {

		@Override
		public CharSequence fixText(CharSequence invalidText) {
			return "";
		}

		@Override
		public boolean isValid(CharSequence text) {
			String universityName = text.toString();
			Iterator<University> it = unis.iterator();
			
			// Check entered text against valid universities
			while (it.hasNext()) {
				if ((it.next().getName()).equals(universityName))
					return true;
			}
			
			return false; // University isn't valid
		}
		
	};
	class ValidateStarter implements OnFocusChangeListener {
		
        @Override
		public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                ((AutoCompleteTextView) v).performValidation();
            }
        }
        
    };
	
}
