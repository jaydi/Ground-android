package anb.ground.activity.main.user;

import anb.ground.R;
import anb.ground.activity.main.FrameActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class UserMainActivity extends FrameActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_main);
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			
			fTransaction.add(R.id.frame_user_main_feeds, new NewsFeedFragment());
			fTransaction.add(R.id.frame_user_main_calendar, new NewsCalendarFragment());
			fTransaction.commit();
		}
	}

}
