package com.wiredave.imoto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Window;

public class SplashActivity extends Activity {

	CTimer refresh_timer;
	String login_flag;
	SharedPreferences preferances;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);


		refresh_timer = new CTimer(3000, 3000);
		refresh_timer.start();

	}

	public class CTimer extends CountDownTimer {
		// Note: Your consumer key and secret should be obfuscated in your
		// source code before shipping.


		public CTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		try {
			refresh_timer.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		try {
			refresh_timer.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
