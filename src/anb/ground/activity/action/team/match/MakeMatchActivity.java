package anb.ground.activity.action.team.match;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import anb.ground.R;
import anb.ground.activity.action.GroundSearchActivity;
import anb.ground.activity.action.TeamSearchActivity;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.dialogs.GroundDatePicker;
import anb.ground.dialogs.TimeRangePicker;
import anb.ground.models.Ground;
import anb.ground.models.LocalUser;
import anb.ground.models.Match;
import anb.ground.models.Team;
import anb.ground.models.TeamHint;
import anb.ground.protocols.CreateMatchResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DateUtils;
import anb.ground.utils.GA;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MakeMatchActivity extends TrackedFragmentActivity implements MapView.MapViewEventListener, GroundDatePicker.GroundDatePickerListener,
		TimeRangePicker.TimeRangePickerListener, LocationListener {
	public static final int REQUEST_GROUND = 344;
	public static final int REQUEST_TEAM = 4573;

	private MapView mapView;
	private MapPOIItem poi;

	private LocationManager locationManager;
	private int locationRequestCounter = 0;

	private TeamHint teamHint;
	private int year, month, day;
	private int startHour, startMin, endHour, endMin;
	private long startTime;
	private long endTime;
	private Ground ground;
	private Team awayTeam;
	private String description;
	private boolean open = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_match);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		teamHint = getIntent().getParcelableExtra(TeamHint.EXTRA_TEAM_HINT);

		poi = new MapPOIItem();
		poi.setMarkerType(MapPOIItem.MarkerType.BluePin);
		poi.setShowCalloutBalloonOnTouch(false);
		poi.setDraggable(false);
	}

	@Override
	public void onPause() {
		super.onPause();
		FrameLayout locationMap = (FrameLayout) findViewById(R.id.match_location_map);
		locationMap.removeView(mapView);
		mapView = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView = new MapView(this);
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);
		mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(LocalUser.lastLocation().getLatitude(), LocalUser.lastLocation().getLongitude()),
				4, false);
		mapView.setMapViewEventListener(this);

		FrameLayout locationMap = (FrameLayout) findViewById(R.id.match_location_map);
		locationMap.addView(mapView);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		setParameters();
	}

	private void setParameters() {
		if (year != 0) {
			TextView matchDate = (TextView) findViewById(R.id.text_view_match_date);
			matchDate.setText(DateUtils.getDateString(DateUtils.getDate(year, month, day)));
		}
		if (startHour != 0) {
			TextView matchTime = (TextView) findViewById(R.id.text_view_match_time);
			matchTime.setText(DateUtils.getTimeString(startHour, startMin) + " ~ " + DateUtils.getTimeString(endHour, endMin));
		}
		if (ground != null) {
			TextView matchLocation = (TextView) findViewById(R.id.text_view_match_location);
			matchLocation.setText(ground.getName());
		}
		if (awayTeam != null) {
			TextView matchOpponent = (TextView) findViewById(R.id.text_view_match_opponent);
			matchOpponent.setText(awayTeam.getName());
		}

		if (poi.getMapPoint() != null) {
			mapView.removePOIItem(poi);
			mapView.setMapCenterPoint(poi.getMapPoint(), false);
			mapView.addPOIItem(poi);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (locationRequestCounter == 0 && mapView != null)
			mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), 4, false);
		locationRequestCounter++;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		teamHint = savedInstanceState.getParcelable("teamHint");
		year = savedInstanceState.getInt("year");
		month = savedInstanceState.getInt("month");
		day = savedInstanceState.getInt("day");
		startHour = savedInstanceState.getInt("startHour");
		startMin = savedInstanceState.getInt("startMin");
		endHour = savedInstanceState.getInt("endHour");
		endMin = savedInstanceState.getInt("endMin");
		ground = savedInstanceState.getParcelable("ground");
		awayTeam = savedInstanceState.getParcelable("awayTeam");
		open = savedInstanceState.getBoolean("open");
		locationRequestCounter = savedInstanceState.getInt("requestCounter");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putParcelable("teamHint", teamHint);
		savedInstanceState.putInt("year", year);
		savedInstanceState.putInt("month", month);
		savedInstanceState.putInt("day", day);
		savedInstanceState.putInt("startHour", startHour);
		savedInstanceState.putInt("startMin", startMin);
		savedInstanceState.putInt("endHour", endHour);
		savedInstanceState.putInt("endMin", endMin);
		savedInstanceState.putParcelable("ground", ground);
		savedInstanceState.putParcelable("awayTeam", awayTeam);
		savedInstanceState.putBoolean("open", open);
		savedInstanceState.putInt("requestCounter", locationRequestCounter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.make_match, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_make_match:
			makeMatch();
			break;
		}

		return true;
	}

	public void getDate(View view) {
		GroundDatePicker datePicker = new GroundDatePicker();
		datePicker.setListener(this);
		datePicker.show(getSupportFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		setParameters();
	}

	public void getTime(View view) {
		TimeRangePicker timePicker = new TimeRangePicker();
		timePicker.setListener(this);
		timePicker.show(getSupportFragmentManager(), "timePicker");
	}

	@Override
	public void onTimeRangePicked(int startHour, int startMin, int endHour, int endMin) {
		this.startHour = startHour;
		this.startMin = startMin;
		this.endHour = endHour;
		this.endMin = endMin;

		setParameters();
	}

	public void getLocation(View view) {
		Intent intent = new Intent(this, GroundSearchActivity.class);
		startActivityForResult(intent, REQUEST_GROUND);
	}

	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
		getLocation(null);
	}

	public void getOpponent(View view) {
		Intent intent = new Intent(this, TeamSearchActivity.class);
		intent.putExtra(TeamSearchActivity.EXTRA_IS_REQUEST, true);
		startActivityForResult(intent, REQUEST_TEAM);
	}

	public void toggleOpen(View view) {
		ImageView imageCheckBox = (ImageView) view;

		if (open) {
			open = false;
			imageCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_checked));
		} else {
			open = true;
			imageCheckBox.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_not_checked));
		}
	}

	private void makeMatch() {
		EditText textDescription = (EditText) findViewById(R.id.edit_text_match_description);
		description = textDescription.getEditableText().toString();

		if (!Validator.validateDate(year, month, day))
			return;

		if (!Validator.validateTimeRange(startHour, startMin, endHour, endMin))
			return;

		startTime = DateUtils.getStamp(year, month, day, startHour, startMin);
		endTime = DateUtils.getStamp(year, month, day, endHour, endMin);

		final Match match = new Match(teamHint.getId(), startTime, endTime, (ground == null) ? 0 : ground.getId(), (awayTeam == null) ? 0 : awayTeam.getId(),
				description, open);

		if (Validator.validateMatch(match))
			GroundClient.createMatch(new ResponseHandler<CreateMatchResponse>() {

				@Override
				public void onReceiveOK(CreateMatchResponse response) {
					Intent intent = new Intent(getApplicationContext(), MatchShowActivity.class);
					intent.putExtra(TeamHint.EXTRA_TEAM_HINT, teamHint);
					intent.putExtra(Match.EXTRA_MATCH_ID, response.getMatchId());
					startActivity(intent);

					// GA logging -------
					trackEvent(GA.CATEGORY_MATCH, GA.ACTION_MAKE, String.valueOf(teamHint.getId()));
					if (match.getAwayTeamId() != 0)
						trackEvent(GA.CATEGORY_MATCH, GA.ACTION_INVITE, String.valueOf(teamHint.getId()));
					// ------------------
					finish();
				}

			}, match);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			if (requestCode == REQUEST_GROUND) {
				ground = data.getParcelableExtra(GroundSearchActivity.EXTRA_GROUND);

				poi.setItemName(ground.getName());
				poi.setMapPoint(MapPoint.mapPointWithGeoCoord(ground.getLatitude(), ground.getLongitude()));
				poi.setMarkerType(MapPOIItem.MarkerType.BluePin);
			} else if (requestCode == REQUEST_TEAM)
				awayTeam = data.getParcelableExtra(TeamSearchActivity.EXTRA_TEAM);
	}

	// unused methods

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
