package anb.ground.activity.action;

import java.util.ArrayList;
import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.adapter.TeamListAdapter;
import anb.ground.dialogs.TeamInfoDialog;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.JoinTeamResponse;
import anb.ground.protocols.SearchTeamNearbyResponse;
import anb.ground.protocols.SearchTeamResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.StringProvider;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TeamSearchActivity extends TrackedFragmentActivity implements TeamInfoDialog.TeamInfoDialogListener, LocationListener {
	public static final String EXTRA_IS_REQUEST = "anb.ground.extra.isRequest";
	public static final String EXTRA_TEAM = "anb.ground.extra.team";
	public static final int DISTANCE_NEAR = 5;

	private boolean isRequest;

	private LocationManager locationManager;
	private int locationRequestCounter = 0;

	private ListView recommendedTeams;
	private ListView searchedTeams;

	private List<TeamInfo> recommendedTeamList = new ArrayList<TeamInfo>();
	private List<TeamInfo> searchedTeamList = new ArrayList<TeamInfo>();

	private TeamListAdapter recommendedTeamListAdapter = new TeamListAdapter(this, recommendedTeamList);
	private TeamListAdapter searchedTeamListAdapter = new TeamListAdapter(this, searchedTeamList);

	private TeamInfo team;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_team);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		isRequest = getIntent().getBooleanExtra(EXTRA_IS_REQUEST, false);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		searchedTeams = (ListView) findViewById(R.id.list_team_search_searched_team);
		recommendedTeams = (ListView) findViewById(R.id.list_team_search_recommended_team);

		setRecommendedTeamsView();
		setSearchedTeamsView();
	}

	private void setRecommendedTeamsView() {
		setRecommendHeader();
		recommendedTeamList.add(new TeamInfo());
		recommendedTeams.setAdapter(recommendedTeamListAdapter);
		recommendedTeams.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View parent, int position, long id) {
				try {
					team = recommendedTeamList.get(--position);
					showTeamDialogue(team);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}

		});
	}

	private void setRecommendHeader() {
		View headerView = getLayoutInflater().inflate(R.layout.list_header_section_divider_layout, null, false);
		TextView text = (TextView) headerView.findViewById(R.id.text_section_header_text);
		text.setText(StringProvider.getString(R.string.teams_nearby));
		recommendedTeams.addHeaderView(headerView);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (locationRequestCounter == 0)
			GroundClient.searchTeamNearby(new ResponseHandler<SearchTeamNearbyResponse>() {

				@Override
				protected void onReceiveOK(SearchTeamNearbyResponse response) {
					List<TeamInfo> teamList = response.getTeamList();
					recommendedTeamList.clear();
					if (teamList != null)
						recommendedTeamList.addAll(teamList);
					recommendedTeamListAdapter.notifyDataSetChanged();
				}

			}, location.getLatitude(), location.getLongitude(), DISTANCE_NEAR);

		locationRequestCounter++;
	}

	private void setSearchedTeamsView() {
		searchedTeams.setAdapter(searchedTeamListAdapter);
		searchedTeams.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View parent, int position, long id) {
				team = searchedTeamList.get(position);
				showTeamDialogue(team);
			}

		});

		EditText textSearchKeyword = (EditText) findViewById(R.id.edit_text_team_search);
		setSearchKeywordListener(textSearchKeyword);
	}

	private void setSearchKeywordListener(EditText textSearchKeyword) {
		textSearchKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (filterLength(s)) {
					recommendedTeams.setVisibility(View.GONE);
					searchedTeams.setVisibility(View.VISIBLE);
					GroundClient.searchTeam(new ResponseHandler<SearchTeamResponse>() {

						@Override
						public void onReceiveOK(SearchTeamResponse response) {
							List<TeamInfo> teamList = response.getTeamList();
							searchedTeamList.clear();
							if (teamList != null)
								searchedTeamList.addAll(teamList);
							searchedTeamListAdapter.notifyDataSetChanged();
						}

					}, s.toString());
				} else {
					if (recommendedTeamList.size() > 0)
						recommendedTeams.setVisibility(View.VISIBLE);
					searchedTeams.setVisibility(View.GONE);
				}
			}

			private boolean filterLength(CharSequence s) {
				return s.length() >= Validator.LENGTH_ENOUGH_KOR;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.team_search, menu);
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

	private void showTeamDialogue(TeamInfo team) {
		// return if the item is dummy
		if (team == null || team.getId() == 0)
			return;

		InputMethodManager iManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		iManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

		TeamInfoDialog dialog = new TeamInfoDialog();
		dialog.setTeam(team);
		dialog.setIsRequest(isRequest);
		dialog.show(getSupportFragmentManager(), "teamInfo");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (isRequest) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra(EXTRA_TEAM, team.toTeam());
			setResult(RESULT_OK, returnIntent);
			finish();
		} else {
			GroundClient.joinTeam(new ResponseHandler<JoinTeamResponse>() {

				@Override
				public void onReceiveOK(JoinTeamResponse response) {
					ToastUtils.show(getApplicationContext().getString(R.string.join_team_notice));
				}

			}, team.getId());
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
