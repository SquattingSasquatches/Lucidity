package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class QuizActivity extends Activity {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ViewFlipper questionViewFlipper;
	private TextView txtQuestionCount, txtCountdown, txtQuestion, txtQuizInfo, txtHeading;
	private ListView answersListView;
	private Button btnStartQuiz;
	
	/* Misc */
	private InternalReceiver submitAnswer, loadQuestions, loadQuiz;
	private Intent nextActivity;
	private Quiz quiz;
	private int userId,
				quizId,
				questionId,
				curQuestion,
				numQuestions,
				quizDuration;
	private String deviceId;
	private ArrayList<Answer> studentAnswers;
	
	InternalReceiver getCourses;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        
        quizId = getIntent().getIntExtra("quizId", -1);
        if (quizId == -1)
        	quizId = Integer.valueOf(getIntent().getStringExtra("quizId"));
        
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        
        studentAnswers = new ArrayList<Answer>();
        questionViewFlipper = (ViewFlipper) findViewById(R.id.questionContainer);
        btnStartQuiz = (Button) findViewById(R.id.btnStartQuiz);
        txtQuizInfo = (TextView) findViewById(R.id.txtQuizInfo);
        txtQuestionCount = (TextView) findViewById(R.id.txtQuestionCount);
        txtCountdown = (TextView) findViewById(R.id.txtCountdown);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        
        userId = localDB.getUserId();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        // Receivers
        /*submitAnswer = new InternalReceiver() {
			public void update(JSONArray data){
				QuizActivity.this.nextQuestion(data);
			}
		};
		submitAnswer.addParam("user_id", userId);
		remoteDB.addReceiver("user.answer.submit", submitAnswer); // or whatever*/
		
		loadQuestions = new InternalReceiver() {
			public void update(JSONArray data) {
				QuizActivity.this.loadQuestions(data);
			}
		};
		loadQuestions.addParam("device_id", deviceId);
		loadQuestions.addParam("quiz_id", quizId);
		remoteDB.addReceiver("user.quiz.take", loadQuestions);
		
		loadQuiz = new InternalReceiver() {
			public void update(JSONArray data) {
				QuizActivity.this.loadQuiz(data);
			}
		};
		loadQuiz.addParam("device_id", deviceId);
		loadQuiz.addParam("quiz_id", quizId);
		remoteDB.addReceiver("user.quiz.view", loadQuiz);
		
		
		loading.setTitle("Please wait");
        loading.setMessage("Loading quiz... ");
        loading.setCancelable(false);
        loading.show();
        
        remoteDB.execute("user.quiz.view");
	}
	
	public void startQuiz() {
		if (localDB.isCheckedIn()) {
			loading.setTitle("Please wait");
	        loading.setMessage("Starting quiz... ");
	        loading.setCancelable(false);
	        loading.show();
	        
	        remoteDB.execute("user.quiz.take");
		} else {
			Toast.makeText(this, "Quiz has started but you are not checked in.", Toast.LENGTH_LONG).show();
		}
	}
	
	public void endQuiz() {
		Toast.makeText(QuizActivity.this, "Quiz complete.", Toast.LENGTH_LONG).show();
		// send studentAnswers to remoteDB
		finish();
	}
	
	public void nextQuestion() {
		Log.i("q", curQuestion + "|" + numQuestions);
		if (curQuestion == numQuestions) {
			endQuiz();
		} else {		
			questionViewFlipper.showNext();
			txtQuestionCount.setText("Question " + ++curQuestion + " of " + numQuestions);
		}
	}
	
	public void loadQuiz(JSONArray data) {
		try {
			JSONObject quiz = data.getJSONObject(0);
			txtHeading.setText(quiz.getString("name"));
			numQuestions = quiz.getInt("num_of_questions");
			quizDuration = quiz.getInt("duration");
			txtQuizInfo.setText("This quiz has " + numQuestions + " question(s).\n" +
						"You will have " + quizDuration/60 + " minute(s) to complete it.");
			
			btnStartQuiz.setOnClickListener(startQuiz);
			loading.dismiss();
			
		} catch (JSONException e) {
			Log.d("loadQuiz", "JSON error");
		}
		
	}
	
	public void loadQuestions(JSONArray data) {
		// get resultcode and check if success possibly
		try {
			data = data.getJSONObject(0).getJSONArray("questions");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int resultLength = data.length();
		quiz = new Quiz();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject question = data.getJSONObject(i);
				JSONArray answers = question.getJSONArray("answers");
				Question qToAdd = new Question(question.getInt("id"), question.getString("text"));
				
				curQuestion = 1;
				
				int numAnswers = question.getInt("num_of_answers");
				
				for (int k = 0; k < numAnswers; ++k) {
					JSONObject answer = answers.getJSONObject(k);
					Answer aToAdd = new Answer(answer.getInt("id"), answer.getString("text"), answer.getInt("question_id"));
					qToAdd.addAnswer(aToAdd);
				}
				
				quiz.addQuestion(qToAdd);
									
			} catch (JSONException e) {
				Log.d("loadQuestions", "JSON error");
			}
		}
	
		for (Question q : quiz.getQuestions()) {
			View questionView = getLayoutInflater().inflate(R.layout.question_container, null);
			txtQuestion = (TextView) questionView.findViewById(R.id.txtQuestion);
			answersListView = (ListView) questionView.findViewById(R.id.answersContainer);
			
			txtQuestion.setText(q.getName());
			answersListView.setAdapter(new AnswersListAdapter<Answer>(this, q.getAnswers()));
			answersListView.setOnItemClickListener(answerClickHandler);
			
			questionViewFlipper.addView(questionView);
		}
		
		txtQuestionCount.setText("Question " + curQuestion + " of " + numQuestions);

		RelativeLayout overlay = (RelativeLayout) findViewById(R.id.layoutOverlay);
		overlay.setVisibility(View.GONE);
		
		loading.dismiss();
	}
	
	private OnItemClickListener answerClickHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = answersListView.getItemAtPosition(position);
			final Answer answer = (Answer) o;
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuizActivity.this);
			alertDialog.setTitle("Are you sure?");
			alertDialog.setMessage("Do you want to submit your answer?");
			alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					studentAnswers.add(answer);
					nextQuestion();
				}
			});
			alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}
	};
	
	
	private final OnClickListener startQuiz = new OnClickListener() {

		@Override
		public void onClick(View v) {
			remoteDB.execute("user.quiz.take");
		}
		
	};
	
}
