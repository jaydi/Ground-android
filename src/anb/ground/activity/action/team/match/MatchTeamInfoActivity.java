package anb.ground.activity.action.team.match;

import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.MatchMemberAdapter;
import anb.ground.adapter.MemberAdapter;
import anb.ground.models.JoinUser;
import anb.ground.models.Match;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.JoinMemberListResponse;
import anb.ground.protocols.PushSurveyResponse;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.GA;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.ToastUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MatchTeamInfoActivity extends TrackedActivity {
	public static final String EXRA_IS_MY_TEAM = "anb.ground.extra.isMyTeam";

	private long matchId;
	private TeamHint teamHint;
	private boolean isMyTeam;

	private TeamInfo teamInfo;
	private List<JoinUser> memberList;

	private ListView members;
	private MatchMemberAdapter matchMemberAdapter;
	private MemberAdapter memberAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_team_info);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		if (savedInstanceState == null) {
			matchId = getIntent().getLongExtra(Match.EXTRA_MATCH_ID, 0);
			teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);
			isMyTeam = getIntent().getBooleanExtra(EXRA_IS_MY_TEAM, false);
		}

		members = (ListView) findViewById(R.id.list_match_team_info_members);

		// GA logging -------
		if (isMyTeam)
			trackEvent(GA.CATEGORY_MATCH_TEAM, GA.ACTION_PLAYERS, String.valueOf(teamHint.getId()));
		else
			trackEvent(GA.CATEGORY_MATCH_TEAM, GA.ACTION_RECORDS, String.valueOf(teamHint.getId()));
		// ------------------
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		matchId = state.getLong("matchId");
		teamHint = state.getParcelable("teamHint");
		isMyTeam = state.getBoolean("isMyTeam");
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		state.putLong("matchId", matchId);
		state.putParcelable("teamHint", teamHint);
		state.putBoolean("isMyTeam", isMyTeam);
	}

	@Override
	public void onResume() {
		super.onResume();

		GroundClient.getTeamInfo(new ResponseHandler<TeamInfoResponse>() {

			@Override
			public void onReceiveOK(TeamInfoResponse response) {
				teamInfo = response.getTeamInfo();
				setHeaderView();
			}

		}, teamHint.getId());
	}

	private void setHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.list_header_team_info_layout, null, false);

		ImageView imageProfile = (ImageView) headerView.findViewById(R.id.image_team_member_header_team_info_picture);
		TextView textDesc = (TextView) headerView.findViewById(R.id.text_team_member_header_team_info_info);

		ImageUtils.getThumnailImageCircular(imageProfile, teamInfo.getImageUrl());
		textDesc.setText("팀명 : " + teamInfo.getName() + "\n지역 : " + teamInfo.getAddress() + "\n연령 : " + teamInfo.getAvgBirth() + "\n팀원 : "
				+ teamInfo.getMembersCount() + "\n전적 : " + teamInfo.getWin() + " 승 " + teamInfo.getDraw() + " 무 " + teamInfo.getLose() + " 패");

		if (members.getHeaderViewsCount() == 0)
			members.addHeaderView(headerView);

		getMemberList();
	}

	private void getMemberList() {
		GroundClient.getJoinMemberList(new ResponseHandler<JoinMemberListResponse>() {

			@Override
			public void onReceiveOK(JoinMemberListResponse response) {
				memberList = response.getUserList();
				setMemberView();
			}

		}, matchId, teamHint.getId());
	}

	private void setMemberView() {
		if (isMyTeam) {
			matchMemberAdapter = new MatchMemberAdapter(this, memberList);
			members.setAdapter(matchMemberAdapter);
		} else {
			memberAdapter = new MemberAdapter(this, memberList);
			members.setAdapter(memberAdapter);
		}

		setButton();
	}

	private void setButton() {
		Button button = (Button) findViewById(R.id.button_match_team_info_action);

		if (isMyTeam) {
			if (teamHint.isManaged())
				button.setText(getResources().getString(R.string.demand_response));
			else
				findViewById(R.id.frame_match_team_info_button_container).setVisibility(View.GONE);
		} else
			button.setText(getResources().getString(R.string.show_records));

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMyTeam) {
					Intent intent = new Intent(getApplicationContext(), PushSurveyActivity.class);
					intent.putExtra(Match.EXTRA_MATCH_ID, matchId);
					intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getApplicationContext(), ShowRecordsActivity.class);
					intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
					startActivity(intent);
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.team_show, menu);
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

	public void demandResponse(View view) {
		GroundClient.pushSurvey(new ResponseHandler<PushSurveyResponse>() {

			@Override
			protected void onReceiveOK(PushSurveyResponse response) {
				ToastUtils.show(R.string.sent_push_survey);

				// GA logging -------
				trackEvent(GA.CATEGORY_SURVEY, GA.ACTION_DEMAND, String.valueOf(teamHint.getId()));
				// ------------------
			}

		}, matchId, teamHint.getId());
	}

	public void showRecords(View view) {

	}
}
