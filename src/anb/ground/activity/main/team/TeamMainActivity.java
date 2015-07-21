package anb.ground.activity.main.team;

import anb.ground.R;
import anb.ground.activity.action.team.board.PostActivity;
import anb.ground.activity.action.team.manage.EditTeamProfileActivity;
import anb.ground.activity.action.team.match.MakeMatchActivity;
import anb.ground.activity.main.FrameActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.dialogs.MemberShowDialog.MemberShowDialogListener;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.MemberActionResponse;
import anb.ground.protocols.TeamInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamMainActivity extends FrameActivity implements MemberShowDialogListener {
	public static final String EXTRA_TEAM_ACTIVITY_POSITION = "anb.ground.extra.teamMain.position";

	private TeamHint teamHint;
	private int position = 0;
	private boolean matchViewPosition = true;

	public TeamHint getTeamHint() {
		return teamHint;
	}

	public void setTeamHint(TeamHint teamHint) {
		this.teamHint = teamHint;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_team_main);
		super.onCreate(savedInstanceState);

		// extracting team_id from intent, for future use
		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);

		getActionBar().setTitle(teamHint.getName());

		if (savedInstanceState == null) {
			position = getIntent().getIntExtra(EXTRA_TEAM_ACTIVITY_POSITION, 0);

			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.team_board_fragment_container, new TeamBoardFragment());
			fragmentTransaction.add(R.id.team_match_fragment_container, new TeamMatchFragment());
			fragmentTransaction.add(R.id.team_match_search_fragment_container, new TeamMatchSearchFragment());
			fragmentTransaction.add(R.id.team_info_fragment_container, new TeamInformationFragment());
			fragmentTransaction.commit();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable("teamHint", teamHint);
		savedInstanceState.putInt("position", position);
		savedInstanceState.putBoolean("matchViewPosition", matchViewPosition);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		teamHint = savedInstanceState.getParcelable("teamHint");
		position = savedInstanceState.getInt("position");
		matchViewPosition = savedInstanceState.getBoolean("matchViewPosition");
		invalidateOptionsMenu();
	}

	@Override
	public void onResume() {
		super.onResume();
		changeFragment(position);
		changeMatchView(matchViewPosition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		switch (position) {
		case 0:
			inflater.inflate(R.menu.team_board, menu);
			break;
		case 1:
			if (teamHint.isManaged())
				inflater.inflate(R.menu.team_match, menu); // new match menu not shown for normal members
			break;
		case 2:
			// TODO ?
			break;
		case 3:
			if (teamHint.isManaged())
				inflater.inflate(R.menu.team_information, menu); // edit team menu not shown for normal members
			break;
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// drawers menu opens when home button clicked
		if (mDrawerToggle.onOptionsItemSelected(item))
			return true;

		// decide and start next activity
		switch (item.getItemId()) {
		case R.id.action_write:
			Intent intent = new Intent(this, PostActivity.class);
			intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
			startActivity(intent);
			break;
		case R.id.action_new_match:
			intent = new Intent(this, MakeMatchActivity.class);
			intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
			startActivity(intent);
			break;
		case R.id.action_team_settings:
			editTeamProfile();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, UserMainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void editTeamProfile() {
		GroundClient.getTeamInfo(new ResponseHandler<TeamInfoResponse>() {

			@Override
			public void onReceiveOK(TeamInfoResponse response) {
				Intent intent = new Intent(getApplicationContext(), EditTeamProfileActivity.class);
				intent.putExtra(TeamInfo.EXTRA_TEAMINFO, response.getTeamInfo());
				startActivity(intent);
			}

		}, teamHint.getId());
	}

	/*
	 * methods for menu button click events
	 */

	public void onClickBoard(View view) {
		changeFragment(0);
	}

	public void onClickMatch(View view) {
		changeFragment(1);
	}

	public void onClickMatchSearch(View view) {
		changeFragment(2);
	}

	public void onClickInfo(View view) {
		changeFragment(3);
	}

	private void changeFragment(int position) {
		this.position = position;

		switch (position) {
		case 0:
			findViewById(R.id.team_board_fragment_container).setVisibility(View.VISIBLE);
			findViewById(R.id.team_match_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_search_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_info_fragment_container).setVisibility(View.INVISIBLE);
			changeMenuView(this.position);
			break;
		case 1:
			findViewById(R.id.team_board_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_fragment_container).setVisibility(View.VISIBLE);
			findViewById(R.id.team_match_search_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_info_fragment_container).setVisibility(View.INVISIBLE);
			changeMenuView(this.position);
			break;
		case 2:
			findViewById(R.id.team_board_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_search_fragment_container).setVisibility(View.VISIBLE);
			findViewById(R.id.team_info_fragment_container).setVisibility(View.INVISIBLE);
			changeMenuView(this.position);
			break;
		case 3:
			findViewById(R.id.team_board_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_search_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_info_fragment_container).setVisibility(View.VISIBLE);
			changeMenuView(this.position);
			break;
		default:
			findViewById(R.id.team_board_fragment_container).setVisibility(View.VISIBLE);
			findViewById(R.id.team_match_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_match_search_fragment_container).setVisibility(View.INVISIBLE);
			findViewById(R.id.team_info_fragment_container).setVisibility(View.INVISIBLE);
			changeMenuView(this.position);
			break;
		}

		invalidateOptionsMenu();
	}

	private void changeMenuView(int position) {
		switch (position) {
		case 0:
			findViewById(R.id.linear_team_main_menu_board).setBackgroundColor(getResources().getColor(R.color.black));
			((ImageView) findViewById(R.id.image_team_main_icon_board)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_board_selected));
			((TextView) findViewById(R.id.text_team_main_menu_text_board)).setTextColor(getResources().getColor(R.color.white));

			findViewById(R.id.linear_team_main_menu_match).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match));
			((TextView) findViewById(R.id.text_team_main_menu_text_match)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match_search).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match_search)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match_search));
			((TextView) findViewById(R.id.text_team_main_menu_text_match_search)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_info).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_info)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_info));
			((TextView) findViewById(R.id.text_team_main_menu_text_info)).setTextColor(getResources().getColor(R.color.black));
			break;
		case 1:
			findViewById(R.id.linear_team_main_menu_board).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_board)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_board));
			((TextView) findViewById(R.id.text_team_main_menu_text_board)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match).setBackgroundColor(getResources().getColor(R.color.black));
			((ImageView) findViewById(R.id.image_team_main_icon_match)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match_selected));
			((TextView) findViewById(R.id.text_team_main_menu_text_match)).setTextColor(getResources().getColor(R.color.white));

			findViewById(R.id.linear_team_main_menu_match_search).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match_search)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match_search));
			((TextView) findViewById(R.id.text_team_main_menu_text_match_search)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_info).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_info)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_info));
			((TextView) findViewById(R.id.text_team_main_menu_text_info)).setTextColor(getResources().getColor(R.color.black));
			break;
		case 2:
			findViewById(R.id.linear_team_main_menu_board).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_board)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_board));
			((TextView) findViewById(R.id.text_team_main_menu_text_board)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match));
			((TextView) findViewById(R.id.text_team_main_menu_text_match)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match_search).setBackgroundColor(getResources().getColor(R.color.black));
			((ImageView) findViewById(R.id.image_team_main_icon_match_search)).setImageDrawable(getResources().getDrawable(
					R.drawable.ic_team_match_search_selected));
			((TextView) findViewById(R.id.text_team_main_menu_text_match_search)).setTextColor(getResources().getColor(R.color.white));

			findViewById(R.id.linear_team_main_menu_info).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_info)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_info));
			((TextView) findViewById(R.id.text_team_main_menu_text_info)).setTextColor(getResources().getColor(R.color.black));
			break;
		case 3:
			findViewById(R.id.linear_team_main_menu_board).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_board)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_board));
			((TextView) findViewById(R.id.text_team_main_menu_text_board)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match));
			((TextView) findViewById(R.id.text_team_main_menu_text_match)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_match_search).setBackgroundColor(getResources().getColor(R.color.background_light));
			((ImageView) findViewById(R.id.image_team_main_icon_match_search)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_match_search));
			((TextView) findViewById(R.id.text_team_main_menu_text_match_search)).setTextColor(getResources().getColor(R.color.black));

			findViewById(R.id.linear_team_main_menu_info).setBackgroundColor(getResources().getColor(R.color.black));
			((ImageView) findViewById(R.id.image_team_main_icon_info)).setImageDrawable(getResources().getDrawable(R.drawable.ic_team_info_selected));
			((TextView) findViewById(R.id.text_team_main_menu_text_info)).setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}

	public void toggleMatchShow(View view) {
		switch (view.getId()) {
		case R.id.text_team_match_settled_matches:
			matchViewPosition = true;
			changeMatchView(matchViewPosition);
			break;
		case R.id.text_team_match_unsettled_matches:
			matchViewPosition = false;
			changeMatchView(matchViewPosition);
			break;
		}
	}

	private void changeMatchView(boolean matchViewPosition) {
		if (matchViewPosition) {
			findViewById(R.id.frame_team_match_unsettled).setVisibility(View.GONE);
			findViewById(R.id.frame_team_match_settled).setVisibility(View.VISIBLE);
			changeSelectorView(matchViewPosition);
		} else {
			findViewById(R.id.frame_team_match_unsettled).setVisibility(View.VISIBLE);
			findViewById(R.id.frame_team_match_settled).setVisibility(View.GONE);
			changeSelectorView(matchViewPosition);
		}
	}

	private void changeSelectorView(boolean matchViewPosition2) {
		if (matchViewPosition) {
			TextView textViewSet = (TextView) findViewById(R.id.text_team_match_settled_matches);
			textViewSet.setBackground(getResources().getDrawable(R.drawable.background_matches_set_selected));
			textViewSet.setTextColor(getResources().getColor(R.color.white));

			TextView textViewUnset = (TextView) findViewById(R.id.text_team_match_unsettled_matches);
			textViewUnset.setBackground(getResources().getDrawable(R.drawable.background_matches_unset_not_selected));
			textViewUnset.setTextColor(getResources().getColor(R.color.black));
		} else {
			TextView textViewSet = (TextView) findViewById(R.id.text_team_match_settled_matches);
			textViewSet.setBackground(getResources().getDrawable(R.drawable.background_matches_set_not_selected));
			textViewSet.setTextColor(getResources().getColor(R.color.black));

			TextView textViewUnset = (TextView) findViewById(R.id.text_team_match_unsettled_matches);
			textViewUnset.setBackground(getResources().getDrawable(R.drawable.background_matches_unset_selected));
			textViewUnset.setTextColor(getResources().getColor(R.color.white));
		}
	}

	// methods for actions binded to included fragments

	// team board fragment actions

	public void showPicture(View view) {
		// TODO
	}
	
	// team match fragment actions
	
	public void refreshMatches() {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.team_match_fragment_container, new TeamMatchFragment());
		fragmentTransaction.commit();
	}

	// team info fragment actions

	public void call(View view) {
		StringBuilder sb = new StringBuilder("tel:");
		sb.append(((TextView) view).getText());
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(sb.toString()));
		startActivity(intent);
	}

	@Override
	public void onMemberShowPositiveClicked(int memberId) {
		GroundClient.acceptMember(new ResponseHandler<MemberActionResponse>() {

			@Override
			public void onReceiveOK(MemberActionResponse response) {
				refreshInformation();
			}

		}, memberId, teamHint.getId());
	}

	@Override
	public void onMemberShowNegativeClicked(int memberId) {
		GroundClient.rejectMember(new ResponseHandler<MemberActionResponse>() {

			@Override
			public void onReceiveOK(MemberActionResponse respone) {
				refreshInformation();
			}

		}, memberId, teamHint.getId());
	}

	public void refreshInformation() {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.team_info_fragment_container, new TeamInformationFragment());
		fragmentTransaction.commit();
	}

}
