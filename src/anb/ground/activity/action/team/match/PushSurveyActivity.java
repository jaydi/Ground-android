package anb.ground.activity.action.team.match;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.adapter.PushMemberSelectionAdapter;
import anb.ground.adapter.PushMemberSelectionAdapter.PushMemberListListener;
import anb.ground.models.JoinUser;
import anb.ground.models.Match;
import anb.ground.models.TeamHint;
import anb.ground.protocols.JoinMemberListResponse;
import anb.ground.protocols.PushTargettedSurveyResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.UserListUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class PushSurveyActivity extends TrackedActivity implements PushMemberListListener {
	private long matchId;
	private TeamHint teamHint;

	private List<JoinUser> unknownList;

	private ListView members;
	private PushMemberSelectionAdapter memberAdapter;

	private boolean selectAll = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_survey);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		matchId = getIntent().getLongExtra(Match.EXTRA_MATCH_ID, 0);
		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);

		members = (ListView) findViewById(R.id.list_push_survey_members);

		GroundClient.getJoinMemberList(new ResponseHandler<JoinMemberListResponse>() {

			@Override
			protected void onReceiveOK(JoinMemberListResponse response) {
				unknownList = UserListUtils.getJoinMemberList(response.getUserList(), Match.JOIN_NONE);
				setAdapter();
			}

		}, matchId, teamHint.getId());
	}

	private void setAdapter() {
		memberAdapter = new PushMemberSelectionAdapter(this, unknownList, this);
		members.setAdapter(memberAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.push_survey, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_send_survey :
			sendSurvey();
			break;
		}

		return true;
	}

	public void selectAll(View view) {
		ImageView button = (ImageView) findViewById(R.id.image_push_survey_select_all);

		if (selectAll) {
			button.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_not_checked));
			selectAll = false;
			setPushAll(false);
		} else {
			button.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_checked));
			selectAll = true;
			setPushAll(true);
		}
	}

	private void setPushAll(boolean pushAll) {
		for (JoinUser member : unknownList)
			member.setToPush(pushAll);
		memberAdapter.notifyDataSetChanged();
	}

	@Override
	public void onSelectMember(JoinUser member) {
		ImageView button = (ImageView) findViewById(R.id.image_push_survey_select_all);
		button.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_not_checked));
		selectAll = false;

		if (member.isToPush())
			unknownList.get(unknownList.indexOf(member)).setToPush(false);
		else
			unknownList.get(unknownList.indexOf(member)).setToPush(true);

		memberAdapter.notifyDataSetChanged();
	}

	private void sendSurvey() {
		List<Integer> pushIds = new ArrayList<Integer>();
		for (JoinUser member : unknownList)
			if (member.isToPush())
				pushIds.add(member.getId());

		GroundClient.pushTargettedSurvey(new ResponseHandler<PushTargettedSurveyResponse>() {

			@Override
			protected void onReceiveOK(PushTargettedSurveyResponse response) {
				ToastUtils.show(R.string.sent_push_survey);
				finish();
			}

		}, matchId, teamHint.getId(), pushIds);
	}
}
