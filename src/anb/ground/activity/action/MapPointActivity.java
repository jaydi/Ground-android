package anb.ground.activity.action;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapReverseGeoCoder.ReverseGeoCodingResultListener;
import net.daum.mf.map.api.MapView;
import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.dialogs.GroundNameDialog;
import anb.ground.models.Ground;
import anb.ground.models.LocalUser;
import anb.ground.protocols.RegisterGroundResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.Validator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MapPointActivity extends TrackedFragmentActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener,
		GroundNameDialog.GroundNameDialogListener, ReverseGeoCodingResultListener, LocationListener {
	public final static int REQUEST_NEW_GROUND = 12324;
	public final static int REQUEST_NEW_SPOT = 35635;
	public final static int REQUEST_SHOW_GROUND = 13423;

	public final static String EXTRA_LATITUDE = "anb.ground.extra.latitude";
	public final static String EXTRA_LONGITUDE = "anb.ground.extra.longitude";
	public final static String EXTRA_ADDRESS = "anb.ground.extra.address";
	public final static String EXTRA_NEW_GROUND = "anb.ground.extra.newGround";

	private MapView mapView;
	private MapPOIItem poi;
	private MapReverseGeoCoder rGeoCoder;

	private LocationManager locationManager;
	private int locationRequestCounter = 0;

	private int requestCode;

	private double latitude;
	private double longitude;
	private String name;
	private String address;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_point);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		requestCode = getIntent().getIntExtra("requestCode", 0);

		mapView = new MapView(this);
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);

		poi = new MapPOIItem();
		poi.setShowCalloutBalloonOnTouch(true);
		poi.setMarkerType(MarkerType.BluePin);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		if (requestCode == REQUEST_SHOW_GROUND) {
			Ground ground = getIntent().getParcelableExtra(EXTRA_NEW_GROUND);

			poi.setItemName(ground.getName() + "\n" + ground.getAddress());
			poi.setMapPoint(MapPoint.mapPointWithGeoCoord(ground.getLatitude(), ground.getLongitude()));
			mapView.addPOIItem(poi);
			mapView.setMapCenterPoint(poi.getMapPoint(), false);
		} else {
			mapView.setMapCenterPointAndZoomLevel(
					MapPoint.mapPointWithGeoCoord(LocalUser.lastLocation().getLatitude(), LocalUser.lastLocation().getLongitude()), 4, false);
			mapView.setMapViewEventListener(this);
			mapView.setPOIItemEventListener(this);
			poi.setDraggable(true);
		}

		FrameLayout mapContent = (FrameLayout) findViewById(R.id.frame_map_content);
		mapContent.addView(mapView);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (locationRequestCounter == 0 && mapView != null && requestCode != REQUEST_SHOW_GROUND)
			mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), 4, false);
		locationRequestCounter++;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (requestCode != REQUEST_SHOW_GROUND)
			getMenuInflater().inflate(R.menu.map_point, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			break;
		case R.id.action_map_point:
			getMapPoint();
			break;
		}

		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		FrameLayout mapContent = (FrameLayout) findViewById(R.id.frame_map_content);
		mapContent.removeView(mapView);
		mapView = null;
	}

	@Override
	public void onMapViewSingleTapped(MapView view, MapPoint point) {
		mapView.removePOIItem(poi);
		poi.setItemName(getString(R.string.use_here));
		poi.setMapPoint(point);
		poi.setMarkerType(MapPOIItem.MarkerType.BluePin);
		mapView.addPOIItem(poi);
	}

	@Override
	public void onCalloutBalloonOfPOIItemTouched(MapView view, MapPOIItem item) {
		getMapPoint();
	}

	private void getMapPoint() {
		if (!Validator.validatePOI(poi))
			return;
		
		latitude = poi.getMapPoint().getMapPointGeoCoord().latitude;
		longitude = poi.getMapPoint().getMapPointGeoCoord().longitude;

		if (requestCode == REQUEST_NEW_GROUND) {
			getGroundNameAndRegister();
		} else if (requestCode == REQUEST_NEW_SPOT) {
			getAddress();
		}
	}

	private void getGroundNameAndRegister() {
		DialogFragment dialog = new GroundNameDialog();
		dialog.show(getSupportFragmentManager(), "groundName");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		EditText textInput = (EditText) ((AlertDialog) dialog.getDialog()).findViewById(R.id.text_input);
		name = textInput.getEditableText().toString();

		getAddress();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();
	}

	private void getAddress() {
		rGeoCoder = new MapReverseGeoCoder(GlobalApplication.LOCAL_API_KEY, poi.getMapPoint(), this, this);
		rGeoCoder.startFindingAddress();
	}

	@Override
	public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder geocoder) {
		address = "address not found";
		geocoder = null;
		returnResultWithAddress();
	}

	@Override
	public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder geocoder, String result) {
		this.address = result;
		geocoder = null;
		returnResultWithAddress();
	}

	private void returnResultWithAddress() {
		if (requestCode == REQUEST_NEW_GROUND)
			registerNewGround();
		else if (requestCode == REQUEST_NEW_SPOT) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra(EXTRA_LATITUDE, latitude);
			returnIntent.putExtra(EXTRA_LONGITUDE, longitude);
			returnIntent.putExtra(EXTRA_ADDRESS, address);
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}

	private void registerNewGround() {
		GroundClient.registerGround(new DialogResponseHandler<RegisterGroundResponse>(DialogUtils.showWaitingDialog(this)) {

			@Override
			public void onReceiveOK(RegisterGroundResponse response) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(EXTRA_NEW_GROUND, new Ground(response.getGroundId(), name, address, latitude, longitude));
				setResult(RESULT_OK, returnIntent);
				finish();
			}

		}, new Ground(name, address, latitude, longitude));
	}

	@Override
	public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1, MapPoint arg2) {
	}

	@Override
	public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
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
