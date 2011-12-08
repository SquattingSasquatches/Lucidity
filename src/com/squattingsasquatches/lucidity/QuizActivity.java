package com.squattingsasquatches.lucidity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class QuizActivity extends Activity {
	
	/* DBs */
	private RemoteDBAdapter remoteDB;
	private LocalDBAdapter localDB;
	
	/* UI */
	private ProgressDialog loading;
	private ViewFlipper questionViewFlipper;
	private TextView txtQuestionCount, txtCountdown, txtQuestion, txtQuizInfo;
	private ListView answersListView;
	
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
	private Timer countdown;
	
	InternalReceiver getCourses;
	
	@Override
	public void onPause() {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
		
		unregisterReceiver(quizBegin);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter("com.squattingsasquatches.lucidity.QUIZ_BEGIN");
        intentFilter.setPriority(1); // throughout the ship
        registerReceiver(quizBegin, intentFilter);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        
        quizId = getIntent().getIntExtra("quizId", -1);
        userId = getIntent().getIntExtra("userId", -1);
        
        questionViewFlipper = (ViewFlipper) findViewById(R.id.questionContainer);
        txtQuizInfo = (TextView) findViewById(R.id.txtQuizInfo);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        countdown = new Timer();
        
        // Receivers
        submitAnswer = new InternalReceiver() {
			public void update(JSONArray data){
				QuizActivity.this.nextQuestion(data);
			}
		};
		submitAnswer.addParam("user_id", userId);
		remoteDB.addReceiver("user.answer.submit", submitAnswer); // or whatever
		
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
	
	public void endQuiz() {
		
	}
	
	public void nextQuestion(JSONArray data) {
		// get resultcode and check if success possibly
		questionViewFlipper.showNext();
		txtQuestionCount.setText("Question " + ++curQuestion + " of " + numQuestions);
		if (curQuestion > numQuestions) {
			endQuiz();
		}
	}
	
	public void loadQuiz(JSONArray data) {
		try {
			JSONObject quiz = data.getJSONObject(0);
			numQuestions = quiz.getInt("num_of_questions");
			quizDuration = quiz.getInt("duration");
			txtQuizInfo.setText("This quiz has " + numQuestions + " questions.\n" +
						"You will have " + quizDuration/60 + " minutes to complete it.");
			
		} catch (JSONException e) {
			Log.d("loadQuiz", "JSON error");
		}
		
	}
	
	public void loadQuestions(JSONArray data) {
		// get resultcode and check if success possibly
		
		int resultLength = data.length();
		
		for (int i = 0; i < resultLength; ++i) {
			try {
				JSONObject question = data.getJSONObject(i);
				JSONArray answers = question.getJSONArray("answers");
				Question qToAdd = new Question(question.getInt("question_id"), question.getString("question_text"));
				
				if (i == 0) questionId = qToAdd.getId();
				
				curQuestion = 1;
				
				int numAnswers = answers.length();
				
				for (int k = 0; k < numAnswers; ++k) {
					JSONObject answer = answers.getJSONObject(k);
					Answer aToAdd = new Answer(answer.getInt("answer_id"), answer.getString("answer_text"));
					qToAdd.addAnswer(aToAdd);
				}
				
				quiz.addQuestion(qToAdd);
									
			} catch (JSONException e) {
				Log.d("loadQuestions", "JSON error");
			}
		}
	
		for (Question q : quiz.getQuestions()) {
			View questionView = getLayoutInflater().inflate(R.layout.question_container, questionViewFlipper);
			txtQuestion = (TextView) questionView.findViewById(R.id.txtQuestion);
			answersListView = (ListView) questionView.findViewById(R.id.answersContainer);
			
			txtQuestion.setText(q.getName());
			answersListView.setAdapter(new ListAdapter<Answer>(this, q.getAnswers()));
			answersListView.setOnItemClickListener(answerClickHandler);
			
			questionViewFlipper.addView(questionView);
		}
		
		txtQuestionCount.setText("Question " + curQuestion + " of " + numQuestions);
		
		countdown.schedule(new TimerTask() {
			@Override
			public void run() {
				txtCountdown.setText(quizDuration-- + " seconds remaining");
				if (quizDuration < 1) {
					endQuiz();
				}
			}
        }, 1000);
		
		LinearLayout overlay = (LinearLayout) findViewById(R.id.layoutOverlay);
		overlay.setVisibility(View.GONE);
		loading.dismiss();
	}
	
	private final OnItemClickListener answerClickHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			Object o = answersListView.getItemAtPosition(position);
			Answer answer = (Answer) o;
			
			submitAnswer.addParam("question_id", questionId);
			submitAnswer.addParam("answer_id", answer.getId());
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuizActivity.this);
			alertDialog.setTitle("Are you sure?");
			alertDialog.setMessage("Do you want to submit your answer?");
			alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					remoteDB.execute("user.answer.submit");
				}
			});
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}
	};
	
	private BroadcastReceiver quizBegin = new BroadcastReceiver() {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			loading.setTitle("Please wait");
	        loading.setMessage("Starting quiz... ");
	        loading.setCancelable(false);
	        loading.show();
	        
	        remoteDB.execute("user.quiz.take");
		}
		
	};
}
