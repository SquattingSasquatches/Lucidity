package com.squattingsasquatches.lucidity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private InternalReceiver submitAnswer, loadQuestions;
	private Intent nextActivity;
	private Quiz quiz;
	private int userId,
				quizId,
				questionId;
	
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        
        quizId = getIntent().getIntExtra("quizId", -1);
        userId = getIntent().getIntExtra("userId", -1);
        
        questionViewFlipper = (ViewFlipper) findViewById(R.id.questionContainer);
        loading = new ProgressDialog(this);
        localDB = new LocalDBAdapter(this).open();
        remoteDB = new RemoteDBAdapter(this);
        
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
		loadQuestions.addParam("user_id", userId);
		loadQuestions.addParam("quiz_id", quizId);
		remoteDB.addReceiver("user.questions.view", loadQuestions);
		
        loading.setTitle("Please wait");
        loading.setMessage("Loading quiz... ");
        loading.setCancelable(false);
        loading.show();
	}
	
	public void nextQuestion(JSONArray data) {
		// get resultcode and check if success possibly
		questionViewFlipper.showNext();
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
					
				}
			});
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}
	};
}
