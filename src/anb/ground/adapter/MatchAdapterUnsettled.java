package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.Match;
import anb.ground.utils.DateUtils;
import anb.ground.utils.MatchListUtils;
import anb.ground.utils.UIScaleUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchAdapterUnsettled extends MatchAdapter {
	private List<Match> homeList;
	private List<Match> awayList;

	private int sizeHome;

	public MatchAdapterUnsettled(Context context, List<Match> matchList, int teamId) {
		super(context, matchList, teamId);
	}

	@Override
	public long getItemId(int position) {
		if (position < sizeHome)
			return homeList.get(position).getId();
		else
			return awayList.get(position - sizeHome).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		sortMatches();
		Match match;
		View view;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (position < sizeHome) {
			match = homeList.get(position);
			view = inflater.inflate(R.layout.list_item_match_unset_home_layout, parent, false);
			setHomeView(view, match);
			if (position == 0)
				view.findViewById(R.id.linear_match_unset_home_container).setBackground(
						context.getResources().getDrawable(R.drawable.selector_match_unset_home_first));
			view.setPadding(UIScaleUtils.getPixels(12), 0, UIScaleUtils.getPixels(12), UIScaleUtils.getPixels(12));
		} else {
			match = awayList.get(position - sizeHome);
			view = inflater.inflate(R.layout.list_item_match_unset_away_layout, parent, false);
			setAwayView(view, match);
			if (position == sizeHome)
				view.findViewById(R.id.linear_match_unset_away_container).setBackground(
						context.getResources().getDrawable(R.drawable.selector_match_unset_away_first));
			view.setPadding(UIScaleUtils.getPixels(12), 0, UIScaleUtils.getPixels(12), UIScaleUtils.getPixels(12));
		}

		setFonts(view);

		return view;
	}

	private void sortMatches() {
		homeList = MatchListUtils.getUnsettledHomeList(matchList, teamId);
		awayList = MatchListUtils.getUnsettledAwayList(matchList, teamId);

		sizeHome = homeList.size();
	}

	private void setAwayView(View view, Match match) {
		TextView textMatchDay = (TextView) view.findViewById(R.id.text_match_day_unset_away);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date_unset_away);
		TextView textMatchOpponent = (TextView) view.findViewById(R.id.text_match_opponent_unset_away);
		TextView textMatchLocation = (TextView) view.findViewById(R.id.text_match_location_unset_away);
		TextView textMatchStatus = (TextView) view.findViewById(R.id.text_match_status_unset_away);
		ImageView imageMatchStatus = (ImageView) view.findViewById(R.id.image_match_status_unset_away);

		textMatchDay.setText(DateUtils.getDay(match.getStartTime()));
		textMatchDate.setText(DateUtils.getDateString(match.getStartTime()));
		textMatchOpponent.setText(match.getOpponentName(teamId));
		textMatchLocation.setText(match.getGroundName());
		switch (match.getStatus()) {
		case Match.INVITE:
			textMatchStatus.setText(context.getResources().getString(R.string.request_no));
			imageMatchStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_match_status_asked_black));
			break;
		case Match.REQUEST:
			textMatchStatus.setText(context.getResources().getString(R.string.request_yes));
			imageMatchStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_match_status_requested_black));
			break;
		}
	}

	private void setHomeView(View view, Match match) {
		TextView textMatchDay = (TextView) view.findViewById(R.id.text_match_day_unset_home);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date_unset_home);
		TextView textMatchOpponent = (TextView) view.findViewById(R.id.text_match_opponent_unset_home);
		TextView textMatchLocation = (TextView) view.findViewById(R.id.text_match_location_unset_home);
		TextView textMatchStatus = (TextView) view.findViewById(R.id.text_match_status_unset_home);
		ImageView imageMatchStatus = (ImageView) view.findViewById(R.id.image_match_status_unset_home);

		textMatchDay.setText(DateUtils.getDay(match.getStartTime()));
		textMatchDate.setText(DateUtils.getDateString(match.getStartTime()));
		textMatchLocation.setText(match.getGroundName());
		switch (match.getStatus()) {
		case Match.HOME_ONLY:
			textMatchOpponent.setText(context.getResources().getString(R.string.no_opponent));
			textMatchStatus.setText(context.getResources().getString(R.string.request_none));
			imageMatchStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_match_status_home_only_green));
			break;
		case Match.INVITE:
			textMatchOpponent.setText(match.getOpponentName(teamId));
			textMatchStatus.setText(context.getResources().getString(R.string.request_yes));
			imageMatchStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_match_status_requested_green));
			break;
		case Match.REQUEST:
			textMatchOpponent.setText(match.getOpponentName(teamId));
			textMatchStatus.setText(context.getResources().getString(R.string.request_no));
			imageMatchStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_match_status_asked_green));
			break;
		}
	}

}
