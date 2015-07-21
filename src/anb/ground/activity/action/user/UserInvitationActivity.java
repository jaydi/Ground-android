package anb.ground.activity.action.user;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.DefaultResponse.StatusCode;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.ToastUtils;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInvitationActivity extends TrackedActivity {
	private int teamId = 33;
	private TeamInfo teamInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_invitation);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		Uri uri = getIntent().getData();
		if (uri != null) {
			String teamId = uri.getQueryParameter("teamId");
			this.teamId = Integer.valueOf(teamId);
		}

		getTeamInfo();
	}

	private void getTeamInfo() {
		GroundClient.getTeamInfo(new ResponseHandler<TeamInfoResponse>() {

			@Override
			public void onReceiveOK(TeamInfoResponse response) {
				teamInfo = response.getTeamInfo();
				if (teamInfo != null)
					setTeamInfoView();
			}

		}, teamId);
	}

	private void setTeamInfoView() {
		ImageView imageProfile = (ImageView) findViewById(R.id.image_team_member_header_team_info_picture);
		TextView textDesc = (TextView) findViewById(R.id.text_team_member_header_team_info_info);

		ImageUtils.getThumnailImageCircular(imageProfile, teamInfo.getImageUrl());
		textDesc.setText("팀명 : " + teamInfo.getName() + "\n지역 : " + teamInfo.getAddress() + "\n연령 : " + teamInfo.getAvgBirth() + "\n팀원 : "
				+ teamInfo.getMembersCount() + "\n전적 : " + teamInfo.getWin() + " 승 " + teamInfo.getDraw() + " 무 " + teamInfo.getLose() + " 패");

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		}
		
		return true;
	}

	public void acceptTeam(View view) {
		GroundClient.acceptTeam(new ResponseHandler<DefaultResponse>() {

			@Override
			protected void onReceiveOK(DefaultResponse response) {
				Intent intent = new Intent(UserInvitationActivity.this, TeamMainActivity.class);
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, new TeamHint(teamInfo.getId(), teamInfo.getName(), teamInfo.getImageUrl(), false));
				startActivity(intent);
				finish();
			}
			
			@Override
			protected void onReceiveError(DefaultResponse response) {
				if (response.getStatusCode() == StatusCode.INVALID_PARAMETERS)
					ToastUtils.show(R.string.alert_already_a_member);
				else
					super.onReceiveError(response);
			}

		}, teamId);
	}
}
