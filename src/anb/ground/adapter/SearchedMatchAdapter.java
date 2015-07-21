package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.models.SMatch;
import anb.ground.utils.DateUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchedMatchAdapter extends BaseAdapter {
	private Context context;
	private List<SMatch> matchList;

	public SearchedMatchAdapter(Context context, List<SMatch> matchList) {
		super();
		this.context = context;
		this.matchList = matchList;
	}

	@Override
	public int getCount() {
		return matchList.size();
	}

	@Override
	public Object getItem(int position) {
		return matchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return matchList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SMatch sMatch = matchList.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// if contained sMatch is dummy, show progress circle
		if (sMatch.getId() == 0)
			return inflater.inflate(R.layout.list_item_loading_big_layout, parent, false);
		if (sMatch.getId() == -1)
			return inflater.inflate(R.layout.list_item_no_results_layout, parent, false);
		
		View view = inflater.inflate(R.layout.list_item_searched_match_layout, parent, false);
		
		TextView textMatchDay = (TextView) view.findViewById(R.id.text_match_day_searched_match);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date_searched_match);
		TextView textMatchLocation = (TextView) view.findViewById(R.id.text_match_location_searched_match);
		TextView textMatchOpponent = (TextView) view.findViewById(R.id.text_match_opponent_searched_match);
		TextView textMatchDistance = (TextView) view.findViewById(R.id.text_match_distance_searched_match);
		
		textMatchDay.setText(DateUtils.getDay(sMatch.getStartTime()));
		textMatchDate.setText(DateUtils.getDateString(sMatch.getStartTime()));
		textMatchLocation.setText(sMatch.getGroundName());
		textMatchOpponent.setText(sMatch.getHomeTeamName());
		textMatchDistance.setText(String.valueOf(sMatch.getDistance()).substring(0, 3) + " km");
		
		setFonts(view);
		
		return view;
	}

	private void setFonts(View view) {
		final ViewGroup mContainer = (ViewGroup) view.getRootView();
		GlobalApplication.setAppFont(mContainer);
	}

}
