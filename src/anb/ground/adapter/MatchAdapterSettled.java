package anb.ground.adapter;

import java.util.List;

import anb.ground.R;
import anb.ground.models.Match;
import anb.ground.utils.DateUtils;
import anb.ground.utils.MatchListUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MatchAdapterSettled extends MatchAdapter {
	private List<Match> upcomingMatches;
	private List<Match> recordingMatches;
	private List<Match> pastMatches;

	private int sizeUpcoming;
	private int sizeRecording;

	public MatchAdapterSettled(Context context, List<Match> matchList, int teamId) {
		super(context, matchList, teamId);
	}

	@Override
	public long getItemId(int position) {
		if (position < sizeUpcoming)
			return upcomingMatches.get(position).getId();
		else if (position >= sizeUpcoming && position < (sizeUpcoming + sizeRecording))
			return recordingMatches.get(position - sizeUpcoming).getId();
		else
			return pastMatches.get(position - (sizeUpcoming + sizeRecording)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		sortMatches();
		Match match;
		View view;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (position < sizeUpcoming) {
			match = upcomingMatches.get(position);
			view = inflater.inflate(R.layout.list_item_match_upcoming_layout, parent, false);
			setUpcomingView(view, match);
		} else if (position >= sizeUpcoming && position < (sizeUpcoming + sizeRecording)) {
			match = recordingMatches.get(position - sizeUpcoming);
			view = inflater.inflate(R.layout.list_item_match_recording_layout, parent, false);
			setRecordingView(view, match);
		} else {
			match = pastMatches.get(position - (sizeUpcoming + sizeRecording));
			view = inflater.inflate(R.layout.list_item_match_past_layout, parent, false);
			setPastView(view, match);
		}

		setFonts(view);

		return view;
	}

	private void sortMatches() {
		upcomingMatches = MatchListUtils.getUpcomingList(matchList);
		recordingMatches = MatchListUtils.getRecordingList(matchList);
		pastMatches = MatchListUtils.getPastList(matchList);

		sizeUpcoming = upcomingMatches.size();
		sizeRecording = recordingMatches.size();
	}

	private void setPastView(View view, Match match) {
		TextView textResult = (TextView) view.findViewById(R.id.text_match_result);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date);
		TextView textMatchOpponent = (TextView) view.findViewById(R.id.text_match_opponent);
		TextView textScoreMine = (TextView) view.findViewById(R.id.text_match_score_myteam);
		TextView textScoreOpponent = (TextView) view.findViewById(R.id.text_match_score_opponent);
		
		switch (match.isWin(teamId)) {
		case Match.WIN :
			textResult.setText("WIN");
			textResult.setBackgroundColor(context.getResources().getColor(R.color.green));
			break;
		case Match.DRAW :
			textResult.setText("DRAW");
			textResult.setBackgroundColor(context.getResources().getColor(R.color.background_dim));
			break;
		case Match.LOSE :
			textResult.setText("LOSE");
			textResult.setBackgroundColor(context.getResources().getColor(R.color.dark));
		}
		textMatchDate.setText(DateUtils.getDateString(match.getStartTime()));
		textMatchOpponent.setText(match.getOpponentName(teamId));
		textScoreMine.setText("" + match.getMyScore(teamId));
		textScoreOpponent.setText("" + match.getOpponentScore(teamId));
	}

	private void setRecordingView(View view, Match match) {
		TextView textMatchDay = (TextView) view.findViewById(R.id.text_match_day_recording);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date_recording);
		TextView textMatchLocation = (TextView) view.findViewById(R.id.text_match_location_recording);
		TextView textMatchExtra = (TextView) view.findViewById(R.id.text_match_opponent_recording);

		textMatchDay.setText(DateUtils.getDay(match.getStartTime()));
		textMatchDate.setText(DateUtils.getDateString(match.getStartTime()));
		textMatchLocation.setText(match.getGroundName());
		textMatchExtra.setText(match.getOpponentName(teamId));
	}

	private void setUpcomingView(View view, Match match) {
		TextView textMatchDay = (TextView) view.findViewById(R.id.text_match_day_upcoming);
		TextView textMatchDate = (TextView) view.findViewById(R.id.text_match_date_upcoming);
		TextView textMatchTime = (TextView) view.findViewById(R.id.text_match_time_upcoming);
		TextView textMatchLocation = (TextView) view.findViewById(R.id.text_match_location_upcoming);
		TextView textMatchExtra = (TextView) view.findViewById(R.id.text_match_opponent_upcoming);

		textMatchDay.setText(DateUtils.getDay(match.getStartTime()));
		textMatchDate.setText(DateUtils.getDateString(match.getStartTime()));
		textMatchTime.setText(DateUtils.getTimeFromStamp(match.getStartTime()));
		textMatchLocation.setText(match.getGroundName());
		textMatchExtra.setText(match.getOpponentName(teamId));
	}

}
