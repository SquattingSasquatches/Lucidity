package com.squattingsasquatches.lucidity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.squattingsasquatches.lucidity.activities.QuizActivity;

public class QuizBegin extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {

		if (CheckInManager.isCheckedIn()) {
			// Start quiz
			Intent quizActivity = new Intent(ctx, QuizActivity.class);
			quizActivity.putExtras(intent.getExtras());
			quizActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(quizActivity);
		} else {
			Toast.makeText(ctx,
					"A quiz has started, but you are not checked in.",
					Toast.LENGTH_LONG).show();
		}
	}

};