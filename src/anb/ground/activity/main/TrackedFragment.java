package anb.ground.activity.main;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class TrackedFragment extends Fragment {
	private EventTracker tracker;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			tracker = (EventTracker) activity;
		} catch (ClassCastException e) {
		}
	}

	public interface EventTracker {
		public void trackEvent(String category, String action, String label);
	}

	protected void trackEvent(String category, String action, String label) {
		tracker.trackEvent(category, action, label);
	}
}
