package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.models.LocalUser;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

public class LoadingActivity extends TrackedActivity {
	
	public final static String EXTRA_MESSAGE = "anb.ground.LoadingActivity.MESSAGE";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SystemClock.sleep(1000);
				
				if (LocalUser.getInstance().isLoggedin()) {
					Intent intent = new Intent(LoadingActivity.this, UserMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				} else {
					Intent intent = new Intent(LoadingActivity.this, GuideActivity.class);
					startActivity(intent);
				}
				
				return null;
			}
			
		}.execute(null, null);
	}

}
