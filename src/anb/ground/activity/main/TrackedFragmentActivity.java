package anb.ground.activity.main;

import anb.ground.activity.main.TrackedFragment.EventTracker;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class TrackedFragmentActivity extends FragmentActivity implements EventTracker {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	@Override
	public void trackEvent(String category, String action, String label) {
		EasyTracker.getInstance(this).send(MapBuilder.createEvent(category, action, label, (long) 0).build());
	}

}
