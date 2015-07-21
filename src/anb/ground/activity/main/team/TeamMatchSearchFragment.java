package anb.ground.activity.main.team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import anb.ground.R;
import anb.ground.activity.action.MapPointActivity;
import anb.ground.activity.main.TrackedFragment;
import anb.ground.adapter.SearchedMatchAdapter;
import anb.ground.dialogs.DateRangePicker;
import anb.ground.dialogs.DateRangePicker.DateRangePickerListener;
import anb.ground.dialogs.MatchInfoDialog;
import anb.ground.dialogs.MatchInfoDialog.MatchInfoDialogListener;
import anb.ground.dialogs.TimeRangePicker;
import anb.ground.dialogs.TimeRangePicker.TimeRangePickerListener;
import anb.ground.models.SMatch;
import anb.ground.models.TeamHint;
import anb.ground.protocols.RequestMatchResponse;
import anb.ground.protocols.SearchMatchResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DateUtils;
import anb.ground.utils.GA;
import anb.ground.utils.StringProvider;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TeamMatchSearchFragment extends TrackedFragment implements LocationListener, MapViewEventListener, DateRangePickerListener, TimeRangePickerListener,
		MatchInfoDialogListener {
	private TeamHint teamHint;

	private long startTime;
	private long endTime;
	private double latitude;
	private double longitude;
	private int distance = 4;

	private int startYear, startMonth, startDay, startHour, startMin;
	private int endYear, endMonth, endDay, endHour, endMin;
	private String address;

	private LocationManager locationManager;

	private ListView searchedMatches;
	private View searchedMatchesHeader;
	private List<SMatch> searchedMatchList = new ArrayList<SMatch>();
	private SearchedMatchAdapter searchedMatchAdapter;

	private boolean autoSearchFlag = true;
	private boolean toggleFlag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_team_match_search, container, false);

		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		teamHint = ((TeamMainActivity) getActivity()).getTeamHint();
		setHeader(inflater, view);
		setDefaults();

		return view;
	}

	private void setDefaults() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(DateUtils.getToday());

		startYear = c.get(Calendar.YEAR);
		startMonth = c.get(Calendar.MONTH);
		startDay = c.get(Calendar.DAY_OF_MONTH);

		c.add(Calendar.DAY_OF_MONTH, 28);

		endYear = c.get(Calendar.YEAR);
		endMonth = c.get(Calendar.MONTH);
		endDay = c.get(Calendar.DAY_OF_MONTH);

		startHour = 0;
		startMin = 0;
		endHour = 23;
		endMin = 59;
	}

	private void setHeader(LayoutInflater inflater, View view) {
		searchedMatches = (ListView) view.findViewById(R.id.list_search_match_lists);
		searchedMatchesHeader = inflater.inflate(R.layout.list_header_searchbox_match_layout, null, false);
		setButtons(searchedMatchesHeader);
		searchedMatches.addHeaderView(searchedMatchesHeader);
	}

	private void setButtons(final View view) {
		ImageView imageRefresh = (ImageView) view.findViewById(R.id.image_match_search_refresh);
		FrameLayout frameScroll = (FrameLayout) view.findViewById(R.id.frame_match_search_toggle_searchbox);
		TextView textDate = (TextView) view.findViewById(R.id.text_match_search_date_range);
		TextView textTime = (TextView) view.findViewById(R.id.text_match_search_time_range);
		TextView textLocation = (TextView) view.findViewById(R.id.text_match_search_location_address);
		Button buttonSearch = (Button) view.findViewById(R.id.button_match_search_search);

		imageRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAutoSearch();
				closeSearchBox(view);
			}

		});
		frameScroll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (toggleFlag)
					closeSearchBox(view);
				else
					openSearchBox(view);
			}

		});
		textDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDateRange();
			}

		});
		textTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getTimeRange();
			}

		});
		textLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getLocation();
			}

		});
		buttonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search();
			}

		});
	}

	private void closeSearchBox(View view) {
		LinearLayout linearSearchbox = (LinearLayout) view.findViewById(R.id.linear_match_search_searchbox);
		final ImageView imageScroll = (ImageView) view.findViewById(R.id.image_match_search_toggle_searchbox);
		imageScroll.setRotation(0);
		linearSearchbox.setVisibility(View.GONE);
		toggleFlag = false;
	}

	private void openSearchBox(View view) {
		LinearLayout linearSearchbox = (LinearLayout) view.findViewById(R.id.linear_match_search_searchbox);
		final ImageView imageScroll = (ImageView) view.findViewById(R.id.image_match_search_toggle_searchbox);
		imageScroll.setRotation(180);
		linearSearchbox.setVisibility(View.VISIBLE);
		toggleFlag = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		setParameters();
		setResults();
		if (autoSearchFlag)
			startAutoSearch();
	}

	private void startAutoSearch() {
		autoSearchFlag = true;
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		showLoadingProgress();
	}

	@Override
	public void onLocationChanged(Location location) {
		if (autoSearchFlag) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			address = StringProvider.getString(R.string.current_location);
			setParameters();
			search();

			stopAutoSearch();
		}
	}

	private void stopAutoSearch() {
		autoSearchFlag = false;
		locationManager.removeUpdates(this);
	}

	private void setParameters() {
		if (startYear != 0 && endYear != 0) {
			String start = DateUtils.getDateString(DateUtils.getDate(startYear, startMonth, startDay));
			String end = DateUtils.getDateString(DateUtils.getDate(endYear, endMonth, endDay));

			TextView textDateRange = (TextView) searchedMatchesHeader.findViewById(R.id.text_match_search_date_range);
			textDateRange.setText(start + " ~ " + end);
		}

		if (startHour != 0 || endHour != 0) {
			TextView textTimeRange = (TextView) searchedMatchesHeader.findViewById(R.id.text_match_search_time_range);
			textTimeRange.setText(DateUtils.getTimeString(startHour, startMin) + " ~ " + DateUtils.getTimeString(endHour, endMin));
		}

		if (latitude != 0 && longitude != 0 && address != null) {
			TextView textAddress = (TextView) searchedMatchesHeader.findViewById(R.id.text_match_search_location_address);
			textAddress.setText(address + " : " + StringProvider.getString(R.string.radius) + " " + distance + "km");
		}
	}

	private void setResults() {
		searchedMatchAdapter = new SearchedMatchAdapter(getActivity(), searchedMatchList);
		searchedMatches.setAdapter(searchedMatchAdapter);
		searchedMatches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showMatch(searchedMatchList.get(--position));
			}

		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	// actions

	// search conditions

	public void getDateRange() {
		DateRangePicker datePicker = new DateRangePicker();
		datePicker.setListener(this);
		datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
	}

	@Override
	public void onDatePicked(int[] numbers) {
		startYear = numbers[0];
		startMonth = numbers[1];
		startDay = numbers[2];
		endYear = numbers[3];
		endMonth = numbers[4];
		endDay = numbers[5];

		setParameters();
	}

	public void getTimeRange() {
		TimeRangePicker timePicker = new TimeRangePicker();
		timePicker.setListener(this);
		timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
	}

	@Override
	public void onTimeRangePicked(int startHour, int startMin, int endHour, int endMin) {
		this.startHour = startHour;
		this.startMin = startMin;
		this.endHour = endHour;
		this.endMin = endMin;

		setParameters();
	}

	@Override
	public void onMapViewSingleTapped(MapView view, MapPoint point) {
		getLocation();
	}

	public void getLocation() {
		Intent intent = new Intent(getActivity(), MapPointActivity.class);
		intent.putExtra("requestCode", MapPointActivity.REQUEST_NEW_SPOT);
		startActivityForResult(intent, MapPointActivity.REQUEST_NEW_SPOT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == MapPointActivity.REQUEST_NEW_SPOT) {
			latitude = data.getDoubleExtra(MapPointActivity.EXTRA_LATITUDE, 0);
			longitude = data.getDoubleExtra(MapPointActivity.EXTRA_LONGITUDE, 0);
			address = data.getStringExtra(MapPointActivity.EXTRA_ADDRESS);
		}
	}

	// match search

	public void search() {
		if (!Validator.validateDateRange(startYear, startMonth, startDay, endYear, endMonth, endDay))
			return;

		if (!Validator.validateTimeRange(startHour, startMin, endHour, endMin))
			return;

		startTime = DateUtils.getStamp(startYear, startMonth, startDay, startHour, startMin);
		endTime = DateUtils.getStamp(endYear, endMonth, endDay, endHour, endMin);

		SMatch sMatch = new SMatch(startTime, endTime, latitude, longitude, distance);

		if (Validator.validateSearchMatch(sMatch)) {
			stopAutoSearch();

			GroundClient.searchMatch(new ResponseHandler<SearchMatchResponse>() {

				@Override
				protected void onReceiveOK(SearchMatchResponse response) {
					searchedMatchList.clear();
					searchedMatchList.addAll(response.getMatchList());
					searchedMatchAdapter.notifyDataSetChanged();
					guide();
				}

			}, sMatch);

			showLoadingProgress();
		}
	}

	private void guide() {
		if (searchedMatchAdapter.getCount() == 0) {
			searchedMatchList.add(new SMatch(-1));
			searchedMatchAdapter.notifyDataSetChanged();
		}
	}

	private void showLoadingProgress() {
		searchedMatchList.clear();
		searchedMatchList.add(new SMatch());
		searchedMatchAdapter.notifyDataSetChanged();
	}

	// match info & match request

	protected void showMatch(SMatch match) {
		// return if item is loading dummy
		if (match.getId() == 0)
			return;

		MatchInfoDialog dialog = new MatchInfoDialog();
		dialog.setMatchId(match.getId());
		dialog.setManaged(teamHint.isManaged());
		dialog.setListener(this);
		dialog.show(getActivity().getSupportFragmentManager(), "matchShow");
	}

	@Override
	public void onRequestMatchClick(final long matchId, int homeTeamId) {
		if (Validator.validateRequestMatch(homeTeamId, teamHint.getId()))
			GroundClient.requestMatch(new ResponseHandler<RequestMatchResponse>() {

				@Override
				protected void onReceiveOK(RequestMatchResponse response) {
					ToastUtils.show(R.string.sent_match_request);
					((TeamMainActivity) getActivity()).refreshMatches();
					search();
					
					// GA logging -------
					trackEvent(GA.CATEGORY_MATCH, GA.ACTION_REQUEST, String.valueOf(teamHint.getId()));
					// ------------------
				}

			}, matchId, homeTeamId, teamHint.getId());
	}

	@Override
	public void onCancelRequestMatchClick(DialogFragment dialog) {
		dialog.dismiss();
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
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