package anb.ground.activity.action.team.match;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import anb.ground.R;
import anb.ground.activity.action.IMActivity;
import anb.ground.activity.action.MapPointActivity;
import anb.ground.activity.action.TeamSearchActivity;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.dialogs.ScorePicker;
import anb.ground.models.Match;
import anb.ground.models.MatchInfo;
import anb.ground.models.Team;
import anb.ground.models.TeamHint;
import anb.ground.protocols.DefaultResponse.StatusCode;
import anb.ground.protocols.InviteTeamResponse;
import anb.ground.protocols.MatchActionResponse;
import anb.ground.protocols.MatchInfoResponse;
import anb.ground.protocols.PushSurveyResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DateUtils;
import anb.ground.utils.GA;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MatchShowActivity extends TrackedFragmentActivity implements MapViewEventListener, ScorePicker.ScorePickerDialogListener {
	public static final int REQUEST_TEAM = 10845;

	private TeamHint teamHint;
	private long matchId;

	private MatchInfo matchInfo;

	private MapView mapView;
	private MapPOIItem poi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_show);
		setFonts();

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);
		matchId = getIntent().getLongExtra(Match.EXTRA_MATCH_ID, 0);
	}

	private void setFonts() {
		TextView textVersus1 = (TextView) findViewById(R.id.text_match_show_versus_1);
		TextView textVersus2 = (TextView) findViewById(R.id.text_match_show_versus_2);
		textVersus1.setTypeface(GlobalApplication.mFont);
		textVersus2.setTypeface(GlobalApplication.mFont);
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();

		mapView = new MapView(this);
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);
		mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), 4, false);
		mapView.setMapViewEventListener(this);

		FrameLayout frameLocationMap = (FrameLayout) findViewById(R.id.frame_match_show_location_map);
		frameLocationMap.addView(mapView);
	}

	private void refresh() {
		GroundClient.getMatchInfo(new ResponseHandler<MatchInfoResponse>() {

			@Override
			public void onReceiveOK(MatchInfoResponse response) {
				matchInfo = response.getMatchInfo();
				setActions();
			}

			@Override
			protected void onReceiveError(MatchInfoResponse response) {
				if (response.getStatusCode() == StatusCode.INVALID_PARAMETERS) {
					ToastUtils.show(R.string.alert_wrong_match_access);
					finish();
				} else
					super.onReceiveError(response);
			}

		}, matchId, teamHint.getId());
	}

	@Override
	public void onPause() {
		super.onPause();
		FrameLayout frameLocationMap = (FrameLayout) findViewById(R.id.frame_match_show_location_map);
		frameLocationMap.removeView(mapView);
		mapView = null;
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		teamHint = state.getParcelable("teamHint");
		matchId = state.getLong("matchId");
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		state.putParcelable("teamHint", teamHint);
		state.putLong("matchId", matchId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.match_show, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		}

		return true;
	}

	private void setActions() {
		switch (matchInfo.getStatus()) {
		case Match.HOME_ONLY:
			setInfoView();
			setJoinView();
			break;
		case Match.INVITE:
			setInfoView();
			setJoinView();
			break;
		case Match.REQUEST:
			setInfoView();
			setJoinView();
			break;
		case Match.MATCHING_COMPLETED:
			setInfoView();
			setJoinView();
			break;
		case Match.READY_SCORE:
			setScoreView();
			break;
		case Match.HOME_SCORE:
			setScoreView();
			break;
		case Match.AWAY_SCORE:
			setScoreView();
			break;
		case Match.SCORE_COMPLETED:
			setScoreView();
			setResultNotice();
			break;
		}

		setDescriptioins();
		setHomeTeamView();
		setAwayTeamView();
	}

	private void setInfoView() {
		ImageView imageBack = (ImageView) findViewById(R.id.image_match_show_status);
		imageBack.setImageDrawable(getResources().getDrawable(R.drawable.background_match_show_1));
		findViewById(R.id.linear_match_show_team_emblems).setVisibility(View.VISIBLE);

		if (!teamHint.isManaged())
			return; // action buttons not shown for normal members

		switch (matchInfo.getStatus()) {
		case Match.HOME_ONLY:
			findViewById(R.id.button_match_show_chatting).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_cancel).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_accept).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_reject).setVisibility(View.GONE);
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.GONE);
			break;
		case Match.INVITE:
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.VISIBLE);
			findViewById(R.id.button_match_show_chatting).setVisibility(View.VISIBLE);
			if (matchInfo.isHome(teamHint.getId()))
				findViewById(R.id.button_match_show_cancel).setVisibility(View.VISIBLE);
			else {
				findViewById(R.id.button_match_show_accept).setVisibility(View.VISIBLE);
				findViewById(R.id.button_match_show_reject).setVisibility(View.VISIBLE);
			}
			break;
		case Match.REQUEST:
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.VISIBLE);
			findViewById(R.id.button_match_show_chatting).setVisibility(View.VISIBLE);
			if (matchInfo.isHome(teamHint.getId())) {
				findViewById(R.id.button_match_show_accept).setVisibility(View.VISIBLE);
				findViewById(R.id.button_match_show_reject).setVisibility(View.VISIBLE);
			} else
				findViewById(R.id.button_match_show_cancel).setVisibility(View.VISIBLE);
			break;
		case Match.MATCHING_COMPLETED:
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.VISIBLE);
			findViewById(R.id.button_match_show_accept).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_reject).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_cancel).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_chatting).setVisibility(View.VISIBLE);
			break;
		}
	}

	private void setScoreView() {
		findViewById(R.id.linear_match_show_member_count).setVisibility(View.INVISIBLE);
		ImageView imageBack = (ImageView) findViewById(R.id.image_match_show_status);
		imageBack.setImageDrawable(getResources().getDrawable(R.drawable.background_match_show_2));

		if (matchInfo.getStatus() == Match.SCORE_COMPLETED)
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.GONE);
		else
			findViewById(R.id.buttonbar_match_show_actions).setVisibility(View.VISIBLE);

		findViewById(R.id.linear_match_show_scoreboard).setVisibility(View.VISIBLE);
		FrameLayout frameHomeScore = (FrameLayout) findViewById(R.id.frame_match_show_home_score);
		FrameLayout frameAwayScore = (FrameLayout) findViewById(R.id.frame_match_show_away_score);
		TextView textHomeScore = (TextView) findViewById(R.id.text_match_show_home_score);
		TextView textAwayScore = (TextView) findViewById(R.id.text_match_show_away_score);
		ImageView imageHomeScore = (ImageView) findViewById(R.id.image_match_show_home_score);
		ImageView imageAwayScore = (ImageView) findViewById(R.id.image_match_show_away_score);

		textHomeScore.setText("" + matchInfo.getHomeScore());
		textAwayScore.setText("" + matchInfo.getAwayScore());

		if (matchInfo.getStatus() == Match.READY_SCORE) {
			if (teamHint.isManaged()) {
				frameHomeScore.setClickable(true);
				frameAwayScore.setClickable(true);
			} // score input not allowed for normal members

			imageHomeScore.setImageDrawable(getResources().getDrawable(R.drawable.background_score));
			imageAwayScore.setImageDrawable(getResources().getDrawable(R.drawable.background_score));
			textHomeScore.setTextColor(getResources().getColor(R.color.white));
			textAwayScore.setTextColor(getResources().getColor(R.color.white));
		} else {
			frameHomeScore.setClickable(false);
			frameAwayScore.setClickable(false);
			imageHomeScore.setImageDrawable(null);
			imageAwayScore.setImageDrawable(null);
			textHomeScore.setTextColor(getResources().getColor(R.color.black));
			textAwayScore.setTextColor(getResources().getColor(R.color.black));
		}

		if (!teamHint.isManaged())
			return; // action buttons not shown for normal members

		switch (matchInfo.getStatus()) {
		case Match.READY_SCORE:
			findViewById(R.id.button_match_show_accept_record).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_adjust_record).setVisibility(View.GONE);
			findViewById(R.id.button_match_show_send_record).setVisibility(View.VISIBLE);
			break;
		case Match.HOME_SCORE:
			if (matchInfo.isHome(teamHint.getId())) {
				findViewById(R.id.button_match_show_send_record).setVisibility(View.GONE);
				findViewById(R.id.text_match_show_sent_record).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.button_match_show_send_record).setVisibility(View.GONE);
				findViewById(R.id.text_match_show_sent_record).setVisibility(View.GONE);
				findViewById(R.id.button_match_show_accept_record).setVisibility(View.VISIBLE);
				findViewById(R.id.button_match_show_adjust_record).setVisibility(View.VISIBLE);
			}
			break;
		case Match.AWAY_SCORE:
			if (matchInfo.isHome(teamHint.getId())) {
				findViewById(R.id.button_match_show_send_record).setVisibility(View.GONE);
				findViewById(R.id.text_match_show_sent_record).setVisibility(View.GONE);
				findViewById(R.id.button_match_show_accept_record).setVisibility(View.VISIBLE);
				findViewById(R.id.button_match_show_adjust_record).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.button_match_show_send_record).setVisibility(View.GONE);
				findViewById(R.id.text_match_show_sent_record).setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	private void setResultNotice() {
		TextView textResult = (TextView) findViewById(R.id.text_match_show_result);
		textResult.setVisibility(View.VISIBLE);
		switch (matchInfo.isWin(teamHint.getId())) {
		case Match.WIN:
			textResult.setText("WIN");
			textResult.setTextColor(getResources().getColor(R.color.green));
			break;
		case Match.LOSE:
			textResult.setText("LOSE");
			textResult.setTextColor(getResources().getColor(R.color.double_black));
			break;
		case Match.DRAW:
			textResult.setText("DRAW");
			textResult.setTextColor(getResources().getColor(R.color.gray));
			break;
		}
	}

	private void setJoinView() {
		findViewById(R.id.frame_match_show_join_bar).setVisibility(View.VISIBLE);
		Button joinStatus = (Button) findViewById(R.id.button_match_show_join_status);

		if (matchInfo.isAskSurvey()) {
			findViewById(R.id.button_match_show_start_survey).setVisibility(View.GONE);

			switch (matchInfo.getJoin()) {
			case Match.JOIN_NONE:
				joinStatus.setVisibility(View.GONE);
				findViewById(R.id.buttonbar_match_show_survey).setVisibility(View.VISIBLE);
				break;
			case Match.JOIN_NO:
				findViewById(R.id.buttonbar_match_show_survey).setVisibility(View.GONE);
				joinStatus.setVisibility(View.VISIBLE);
				joinStatus.setText(getString(R.string.im_not_in));
				break;
			case Match.JOIN_YES:
				findViewById(R.id.buttonbar_match_show_survey).setVisibility(View.GONE);
				joinStatus.setVisibility(View.VISIBLE);
				joinStatus.setText(getString(R.string.im_in));
				break;
			}
		} else if (teamHint.isManaged())
			findViewById(R.id.button_match_show_start_survey).setVisibility(View.VISIBLE); // start survey button not shown for normal members
	}

	private void setDescriptioins() {
		TextView textTime = (TextView) findViewById(R.id.text_match_show_time);
		TextView textLocation = (TextView) findViewById(R.id.text_match_show_location);

		textTime.setText(DateUtils.getDateString(matchInfo.getStartTime()) + " "
				+ DateUtils.getMatchTimeString(matchInfo.getStartTime(), matchInfo.getEndTime()));
		textLocation.setText(matchInfo.getGround().getName());

		setMap();
	}

	private void setMap() {
		poi = new MapPOIItem();
		poi.setItemName(matchInfo.getGround().getName());
		poi.setDraggable(false);
		poi.setShowCalloutBalloonOnTouch(false);
		poi.setMapPoint(MapPoint.mapPointWithGeoCoord(matchInfo.getGround().getLatitude(), matchInfo.getGround().getLongitude()));
		poi.setMarkerType(MarkerType.BluePin);

		if (mapView != null) {
			mapView.setMapCenterPoint(poi.getMapPoint(), false);
			mapView.addPOIItem(poi);
		}
	}

	private void setHomeTeamView() {
		TextView textHome = (TextView) findViewById(R.id.text_match_show_home);
		TextView textHomeCount = (TextView) findViewById(R.id.text_match_show_home_member_count);
		ImageView imageHome = (ImageView) findViewById(R.id.image_match_show_home);
		textHome.setText(matchInfo.getHomeTeamName());
		textHomeCount.setText("" + matchInfo.getHomeJoinedMembersCount());

		if (matchInfo.getStatus() <= Match.MATCHING_COMPLETED)
			ImageUtils.getThumnailImageCircular(imageHome, matchInfo.getHomeImageUrl());
	}

	private void setAwayTeamView() {
		TextView textAway = (TextView) findViewById(R.id.text_match_show_away);
		TextView textAwayCount = (TextView) findViewById(R.id.text_match_show_away_member_count);
		ImageView imageAway = (ImageView) findViewById(R.id.image_match_show_away);

		if (matchInfo.getStatus() > Match.HOME_ONLY) {
			textAway.setText(matchInfo.getAwayTeamName());
			textAwayCount.setText("" + matchInfo.getAwayJoinedMembersCount());

			if (matchInfo.getStatus() <= Match.MATCHING_COMPLETED) {
				imageAway.setImageDrawable(getResources().getDrawable(R.drawable.ic_default_team));
				ImageUtils.getThumnailImageCircular(imageAway, matchInfo.getAwayImageUrl());
			}
		} else {
			textAway.setText("--");
			textAwayCount.setText("--");
			imageAway.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_team));
		}
	}

	public void showHomeTeamInfo(View view) {
		boolean isMyTeam = matchInfo.getHomeTeamId() == teamHint.getId();
		showMatchTeamInfo(new TeamHint(matchInfo.getHomeTeamId(), matchInfo.getHomeTeamName(), matchInfo.getHomeImageUrl(), (isMyTeam) ? teamHint.isManaged()
				: false), isMyTeam);
	}

	public void showAwayTeamInfo(View view) {
		if (matchInfo.getStatus() == Match.HOME_ONLY) {
			Intent intent = new Intent(this, TeamSearchActivity.class);
			intent.putExtra(TeamSearchActivity.EXTRA_IS_REQUEST, true);
			startActivityForResult(intent, REQUEST_TEAM);
		} else {
			boolean isMyTeam = matchInfo.getAwayTeamId() == teamHint.getId();
			showMatchTeamInfo(
					new TeamHint(matchInfo.getAwayTeamId(), matchInfo.getAwayTeamName(), matchInfo.getAwayImageUrl(), (isMyTeam) ? teamHint.isManaged() : false),
					isMyTeam);
		}
	}

	public void showMatchTeamInfo(TeamHint teamHint, boolean isMyTeam) {
		Intent intent = new Intent(this, MatchTeamInfoActivity.class);
		intent.putExtra(Match.EXTRA_MATCH_ID, matchId);
		intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
		intent.putExtra(MatchTeamInfoActivity.EXRA_IS_MY_TEAM, isMyTeam);
		startActivity(intent);
	}

	public void setScore(View view) {
		ScorePicker scorePicker = new ScorePicker();
		scorePicker.setScores(matchInfo.getHomeScore(), matchInfo.getAwayScore());
		scorePicker.show(getSupportFragmentManager(), "scorePicker");
	}

	@Override
	public void onPositiveClick(DialogFragment dialog) {
		NumberPicker homeScorePicker = (NumberPicker) ((AlertDialog) dialog.getDialog()).findViewById(R.id.number_score_picker_home);
		NumberPicker awayScorePicker = (NumberPicker) ((AlertDialog) dialog.getDialog()).findViewById(R.id.number_score_picker_away);

		matchInfo.setHomeScore(homeScorePicker.getValue());
		matchInfo.setAwayScore(awayScorePicker.getValue());

		setScoreView();
	}

	@Override
	public void onNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
	}

	public void chatting(View view) {
		Intent intent = new Intent(this, IMActivity.class);
		intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
		intent.putExtra(Match.EXTRA_MATCH_ID, matchId);
		startActivity(intent);
	}

	public void accept(View view) {
		GroundClient.acceptMatch(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setStatus(response.getMatchStatus());
				setActions();

				// GA logging -------
				trackEvent(GA.CATEGORY_MATCH, GA.ACTION_ACCEPT, String.valueOf(teamHint.getId()));
				// ------------------
			}

		}, matchId);
	}

	public void reject(View view) {
		GroundClient.rejectMatch(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				if (matchInfo.isHome(teamHint.getId())) {
					matchInfo.setStatus(response.getMatchStatus());
					setActions();
				} else
					finish();

				// GA logging -------
				trackEvent(GA.CATEGORY_MATCH, GA.ACTION_REJECT, String.valueOf(teamHint.getId()));
				// ------------------
			}

		}, matchId, teamHint.getId());
	}

	public void cancel(View view) {
		GroundClient.cancelMatch(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				if (matchInfo.isHome(teamHint.getId())) {
					matchInfo.setStatus(response.getMatchStatus());
					setActions();
				} else
					finish();

				// GA logging -------
				trackEvent(GA.CATEGORY_MATCH, GA.ACTION_CANCEL, String.valueOf(teamHint.getId()));
				// ------------------
			}

		}, matchId, teamHint.getId());
	}

	public void sendRecord(View view) {
		GroundClient.sendRecord(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setStatus(response.getMatchStatus());
				setActions();
			}

		}, matchId, teamHint.getId(), matchInfo.getHomeScore(), matchInfo.getAwayScore());
	}

	public void acceptRecord(View view) {
		GroundClient.acceptRecord(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setStatus(response.getMatchStatus());
				setActions();
			}

		}, matchId, teamHint.getId());
	}

	public void adjustRecord(View view) {
		matchInfo.setStatus(Match.READY_SCORE);
		setScoreView();
		setScore(null);
	}

	public void startSurvey(View view) {
		GroundClient.startSurvey(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setAskSurvey(true);
				setJoinView();
				pushSurvey();
			}

		}, matchId, teamHint.getId());
	}

	public void pushSurvey() {
		GroundClient.pushSurvey(new ResponseHandler<PushSurveyResponse>() {

			@Override
			protected void onReceiveOK(PushSurveyResponse response) {
				ToastUtils.show(R.string.sent_push_survey);

				// GA logging -------
				trackEvent(GA.CATEGORY_SURVEY, GA.ACTION_FIRST, String.valueOf(teamHint.getId()));
				// ------------------
			}

		}, matchId, teamHint.getId());
	}

	public void join(View view) {
		GroundClient.joinMatch(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setJoin(Match.JOIN_YES);
				setJoinView();
				ToastUtils.show(R.string.sent_join_yes);
			}

		}, matchId, teamHint.getId(), true);
	}

	public void notJoin(View view) {
		GroundClient.joinMatch(new ResponseHandler<MatchActionResponse>() {

			@Override
			public void onReceiveOK(MatchActionResponse response) {
				matchInfo.setJoin(Match.JOIN_NO);
				setJoinView();
				ToastUtils.show(R.string.sent_join_no);
			}

		}, matchId, teamHint.getId(), false);
	}

	public void showSurvey(View view) {
		matchInfo.setJoin(Match.JOIN_NONE);
		setJoinView();
	}

	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
		Intent intent = new Intent(this, MapPointActivity.class);
		intent.putExtra("requestCode", MapPointActivity.REQUEST_SHOW_GROUND);
		intent.putExtra(MapPointActivity.EXTRA_NEW_GROUND, matchInfo.getGround());
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			if (requestCode == REQUEST_TEAM) {
				final Team team = data.getParcelableExtra(TeamSearchActivity.EXTRA_TEAM);
				if (!Validator.validateInviteTeam(team.getId(), teamHint.getId()))
					return;
				GroundClient.inviteTeam(new ResponseHandler<InviteTeamResponse>() {

					@Override
					public void onReceiveOK(InviteTeamResponse response) {
						matchInfo.setStatus(response.getMatchStatus());
						matchInfo.setAwayTeamId(team.getId());
						matchInfo.setAwayTeamName(team.getName());
						matchInfo.setAwayImageUrl(team.getImageUrl());
						setActions();

						// GA logging -------
						trackEvent(GA.CATEGORY_MATCH, GA.ACTION_INVITE, String.valueOf(teamHint.getId()));
						// ------------------
					}

				}, matchInfo.getId(), matchInfo.getHomeTeamId(), team.getId());
			}
	}

	@Override
	public void onMapViewCenterPointMoved(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewDoubleTapped(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewInitialized(MapView arg0) {
	}

	@Override
	public void onMapViewLongPressed(MapView arg0, MapPoint arg1) {
	}

	@Override
	public void onMapViewZoomLevelChanged(MapView arg0, int arg1) {
	}

}
