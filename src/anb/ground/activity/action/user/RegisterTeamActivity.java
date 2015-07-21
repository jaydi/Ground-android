package anb.ground.activity.action.user;

import java.util.List;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapReverseGeoCoder.ReverseGeoCodingResultListener;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import anb.ground.R;
import anb.ground.activity.action.MapPointActivity;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.activity.main.team.TeamMainActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.LocalUser;
import anb.ground.models.TeamHint;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.RegisterTeamResponse;
import anb.ground.protocols.UploadImageResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.Validator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterTeamActivity extends TrackedActivity implements MapViewEventListener, LocationListener, ReverseGeoCodingResultListener {
	public final static int REQUEST_PICK_IMAGE = 125423;

	private MapView mapView;
	private MapPOIItem poi;
	private MapReverseGeoCoder rGeoCoder;

	private LocationManager locationManager;

	private String teamName;
	private String teamImageUrl;
	private double latitude;
	private double longitude;
	private String address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_team);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register_team, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_make_team:
			makeTeam();
			break;
		}

		return true;
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

		if (poi != null) {
			mapView.addPOIItem(poi);
			mapView.setMapCenterPointAndZoomLevel(poi.getMapPoint(), 4, false);
		}

		FrameLayout locationMap = (FrameLayout) findViewById(R.id.frame_register_team_location_map);
		locationMap.addView(mapView);

		ImageView imageTeam = (ImageView) findViewById(R.id.image_register_team_picture);
		ImageUtils.getThumnailImage(imageTeam, teamImageUrl);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mapView != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			MapPoint point = MapPoint.mapPointWithGeoCoord(latitude, longitude);

			mapView.setMapCenterPointAndZoomLevel(point, 4, false);
			getAddress(point);

			locationManager.removeUpdates(this);
		}
	}

	private void getAddress(MapPoint point) {
		rGeoCoder = new MapReverseGeoCoder(GlobalApplication.LOCAL_API_KEY, point, this, this);
		rGeoCoder.startFindingAddress();
	}

	@Override
	public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder geocoder) {
		geocoder = null;
	}

	@Override
	public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder geocoder, String result) {
		geocoder = null;
		address = result;
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		teamName = state.getString("teamName");
		teamImageUrl = state.getString("teamImageUrl");
		latitude = state.getDouble("latitude");
		longitude = state.getDouble("longitude");
	}

	@Override
	public void onPause() {
		super.onPause();
		FrameLayout locationMap = (FrameLayout) findViewById(R.id.frame_register_team_location_map);
		locationMap.removeView(mapView);
		mapView = null;
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("teamName", teamName);
		state.putString("teamImageUrl", teamImageUrl);
		state.putDouble("latitude", latitude);
		state.putDouble("longitude", longitude);
	}

	public void getImage(View view) {
		Intent imgIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);

		PackageManager manager = getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(imgIntent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe)
			startActivityForResult(imgIntent, REQUEST_PICK_IMAGE);
	}

	@Override
	public void onMapViewSingleTapped(MapView arg0, MapPoint arg1) {
		getTeamLocation(null);
	}

	public void getTeamLocation(View view) {
		Intent intent = new Intent(this, MapPointActivity.class);
		intent.putExtra("requestCode", MapPointActivity.REQUEST_NEW_SPOT);
		startActivityForResult(intent, MapPointActivity.REQUEST_NEW_SPOT);
	}

	public void makeTeam() {
		EditText editTextTeamName = (EditText) findViewById(R.id.edit_text_register_team_name);
		teamName = editTextTeamName.getEditableText().toString();

		final TeamInfo info = new TeamInfo(teamName, teamImageUrl, address, latitude, longitude);

		if (Validator.validateRegisterTeam(info))
			if (Validator.isEmpty(info.getImageUrl()))
				registerTeam(info);
			else
				registerTeamWithImage(info);

	}

	private void registerTeam(final TeamInfo info) {
		GroundClient.registerTeam(new DialogResponseHandler<RegisterTeamResponse>(DialogUtils.showWaitingDialog(RegisterTeamActivity.this)) {

			@Override
			public void onReceiveOK(RegisterTeamResponse response) {
				Log.i("response", response.toString());

				int teamId = response.getTeamId();
				Intent intent = new Intent(getApplicationContext(), TeamMainActivity.class);
				intent.putExtra(TeamHint.EXTRA_TEAM_HINT, new TeamHint(teamId, info.getName(), info.getImageUrl(), true));
				intent.putExtra(TeamMainActivity.EXTRA_TEAM_ACTIVITY_POSITION, 3);
				startActivity(intent);
			}

		}, info);
	}

	private void registerTeamWithImage(final TeamInfo info) {
		ImageUtils.uploadImage(new DialogResponseHandler<UploadImageResponse>(DialogUtils.showWaitingDialog(RegisterTeamActivity.this)) {

			@Override
			public void onReceiveOK(UploadImageResponse response) {
				info.setImageUrl(response.getPath());
				registerTeam(info);
			}

		}, teamImageUrl);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri image = data.getData();
			String[] projection = { Media.DATA };

			Cursor cursor = getContentResolver().query(image, projection, null, null, null);
			cursor.moveToFirst();

			int column = cursor.getColumnIndex(Media.DATA);
			teamImageUrl = cursor.getString(column);

			cursor.close();
		} else if (requestCode == MapPointActivity.REQUEST_NEW_SPOT && resultCode == RESULT_OK && data != null) {
			latitude = data.getDoubleExtra(MapPointActivity.EXTRA_LATITUDE, 0);
			longitude = data.getDoubleExtra(MapPointActivity.EXTRA_LONGITUDE, 0);
			address = data.getStringExtra(MapPointActivity.EXTRA_ADDRESS);

			poi = new MapPOIItem();
			poi.setDraggable(false);
			poi.setShowCalloutBalloonOnTouch(false);
			poi.setItemName(address);
			poi.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
			poi.setMarkerType(MarkerType.BluePin);

			TextView textTeamLocation = (TextView) findViewById(R.id.text_register_team_location);
			textTeamLocation.setText(address);
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
