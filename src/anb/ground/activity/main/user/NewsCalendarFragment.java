package anb.ground.activity.main.user;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class NewsCalendarFragment extends TrackedFragment {
	public NewsCalendarFragment() {
	}
	
	private OnDateChangeListener dateListener = new OnDateChangeListener() {

		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_calendar, container, false);

		setContents(rootView);

		return rootView;
	}

	protected void setContents(View rootView) {
		CalendarView calendarView = (CalendarView) rootView.findViewById(R.id.news_calendar);

		calendarView.setOnDateChangeListener(dateListener);
	}

}
