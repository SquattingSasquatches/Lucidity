package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private TextView txtQuestionCount, txtCountdown, txtQuestion, txtNumQuestionsInfo, txtTimeLimitInfo, txtHeading;
	private ListView answersListView;
	private Button btnStartQuiz;
	private Countdown countdown;
	
	/* Misc */
	private InternalReceiver submitAnswers, loadQuestions, loadQuiz;
	private Quiz quiz;
	private int quizId,
				curQuestion,
				numQuestions,
				quizDuration;
	private String deviceId;
	private ArrayList<Answer> studentAnswers;
	
	
	InternalReceiver getCourses;
	
	@Override
	public void onPause() throws IllegalArgumentException {
		super.onPause();
		if (remoteDB != null)
			remoteDB.unregisterAllReceivers();
		if (localDB != null)
			localDB.close();
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        
        quizId = getIntent().getIntExtra("quizId", -1); // coming from CourseHomeActivity (not yet implemented)
        if (quizId == -1)
        	quizId = Integer.valueOf(getIntent().getStringExtra("quizId")); // coming from C2DMReceiver
        
        studentAnswers = new ArrayList<Answer>();
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        questionViewFlipper = (ViewFlipper) findViewById(R.id.questionContainer);
        btnStartQuiz = (Button) findViewById(R.id.btnStartQuiz);
        txtNumQuestionsInfo = (TextView) findViewById(R.id.txtNumQuestionsInfo);
        txtTimeLimitInfo = (TextView) findViewById(R.id.txtTimeLimitInfo);
        txtQuestionCount = (TextView) findViewById(R.id.txtQuestionCount);
        txtCountdown = (TextView) findViewById(R.id.txtCountdown);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        
        // Receivers
        submitAnswers = new InternalReceiver() {
			public void update(JSONArray data){
				finish();
			}
		};
		submitAnswers.addParam("device_id", deviceId);
		submitAnswers.addParam("quiz_id", quizId);
		
		
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
		loading.setTitle("Please wait");
        loading.setMessage("Starting quiz... ");
        loading.setCancelable(false);
        loading.show();
        
        remoteDB.execute("user.quiz.take");
	}
	
	public void endQuiz() {
		Toast.makeText(QuizActivity.this, "Quiz complete.", Toast.LENGTH_LONG).show();
		countdown.cancel();
		// send studentAnswers to remoteDB
		int numAnswered = studentAnswers.size();
		for (int i = 0; i < numQuestions; ++i) {
			Answer a;
			
			if (i < numAnswered)
				a = studentAnswers.get(i);
			else
				a = new Answer(-1, "DID NOT ANSWER", quiz.getQuestions().get(i).getId());
			
			submitAnswers.addParam("question_results[" + i + "][question_id]", a.getQuestionId());
			submitAnswers.addParam("question_results[" + i + "][selected_answer_id]", a.getId());
		}
		remoteDB.addReceiver("user.quizresult.add", submitAnswers);
		remoteDB.execute("user.quizresult.add");
	}
	
	public void nextQuestion() {
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
			txtNumQuestionsInfo.setText("This quiz has " + numQuestions + " question(s).");
			txtTimeLimitInfo.setText("You will have " + quizDuration/60 + " minute(s) to complete it.");
			
			btnStartQuiz.setOnClickListener(startQuiz);
			loading.dismiss();
			
		} catch (JSONException e) {
			Log.d("loadQuiz", "JSON error");
		}
		
	}
	
	public void loadQuestions(JSONArray data) {
		
		countdown = new Countdown(quizDuration * 1000, 1000);
		JSONArray questions = new JSONArray();
		
		try {
			questions = data.getJSONObject(0).getJSONArray("questions");
		} catch (JSONException e) {
			Log.e("loadQuestions", "Invalid JSON returned by quiz.take");
		}
		
		int numQuestions = questions.length();
		quiz = new Quiz();
		
		for (int i = 0; i < numQuestions; ++i) {
			try {
				JSONObject question = questions.getJSONObject(i);
				JSONArray answers = question.getJSONArray("answers");
				Question qToAdd = new Question(question.getInt("id"), question.getString("text"));
				
				int numAnswers = question.getInt("num_of_answers");
				curQuestion = 1;
				
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
			answersListView.setAdapter(new ListAdapter<Answer>(this, q.getAnswers(), R.layout.answers_list_item));
			answersListView.setOnItemClickListener(answerClickHandler);
			
			questionViewFlipper.addView(questionView);
		}
		
		txtQuestionCount.setText("Question " + curQuestion + " of " + numQuestions);

		RelativeLayout overlay = (RelativeLayout) findViewById(R.id.layoutOverlay);
		overlay.setVisibility(View.GONE);
		
		loading.dismiss();
		countdown.start();
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
	
	private class Countdown extends CountDownTimer {

		public Countdown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			txtCountdown.setText("Time's up!");
			endQuiz();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			txtCountdown.setText(millisUntilFinished/1000 + " seconds remaining");
		}
		
	};
	
}
