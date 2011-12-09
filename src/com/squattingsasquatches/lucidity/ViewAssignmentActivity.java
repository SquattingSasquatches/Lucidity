package com.squattingsasquatches.lucidity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAssignmentActivity extends Activity {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private TextView txtHeading, txtDescription, txtDueDate;
	private Button btnViewDocument;
	
	/* Misc */
	private Assignment assignment;
	private SimpleDateFormat df;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_assignment);
		
		txtHeading = (TextView) findViewById(R.id.txtHeading);
		txtDescription = (TextView) findViewById(R.id.txtDescription);
		txtDueDate = (TextView) findViewById(R.id.txtDueDate);
		btnViewDocument = (Button) findViewById(R.id.btnViewDocument);

        remoteDB = new RemoteDBAdapter(this);
        localDB = new LocalDBAdapter(this).open();
        
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
			assignment = new Assignment(
									getIntent().getIntExtra("assignmentId", -1),
									getIntent().getStringExtra("assignmentName"),
									getIntent().getIntExtra("assignmentDocId", -1),
									getIntent().getStringExtra("assignmentDescription"),
									(Date) df.parse(getIntent().getStringExtra("assignmentDueDate")));
			
			txtHeading.setText(assignment.getName());
	        txtDescription.setText(assignment.getDescription());
	        
	        df = new SimpleDateFormat("h:mm:ss a 'on' EEE, MMM d, yyyy");
	        
	        txtDueDate.setText(df.format(assignment.getDueDate()));
		} catch (ParseException e) {
			Log.e("ViewAssignment", "ParseException on assignment due date");
		}
        
        btnViewDocument.setOnClickListener(viewDocument);
	}
	
	private OnClickListener viewDocument = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Toast.makeText(ViewAssignmentActivity.this, "Not yet implemented", Toast.LENGTH_LONG).show();
		}
		
	};
}
