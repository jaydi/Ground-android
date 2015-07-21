package anb.ground.activity.action.team.manage;

import java.util.List;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import anb.ground.R;
import anb.ground.activity.action.MapPointActivity;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.TeamInfo;
import anb.ground.protocols.EditTeamProfileResponse;
import anb.ground.protocols.UploadImageResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.Validator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class EditTeamProfileActivity extends TrackedActivity implements MapViewEventListener {
	public final static int REQUEST_PICK_IMAGE = 125413;

	private TeamInfo teamInfo;

	private MapView mapView;
	private MapPOIItem poi = new MapPOIItem();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_team_profile);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));

		teamInfo = getIntent().getParcelableExtra(TeamInfo.EXTRA_TEAMINFO);

		poi.setDraggable(false);
		poi.setShowCalloutBalloonOnTouch(false);
		poi.setMarkerType(MarkerType.BluePin);
	}

	@Override
	public void onResume() {
		super.onResume();

		ImageView imageView = (ImageView) findViewById(R.id.image_edit_team_profile_picture);
		TextView textName = (TextView) findViewById(R.id.text_edit_team_profile_team_name);
		TextView textAddress = (TextView) findViewById(R.id.text_edit_team_profile_location);

		if (!Validator.isEmpty(teamInfo.getImageUrl()))
			ImageUtils.getThumnailImage(imageView, teamInfo.getImageUrl());
		
		textName.setText(teamInfo.getName());
		textAddress.setText(teamInfo.getAddress());

		mapView = new MapView(this);
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);
		mapView.setMapViewEventListener(this);

		poi.setItemName(teamInfo.getAddress());
		poi.setMapPoint(MapPoint.mapPointWithGeoCoord(teamInfo.getLatitude(), teamInfo.getLongitude()));

		FrameLayout mapFrame = (FrameLayout) findViewById(R.id.frame_edit_team_profile_location_map);
		mapFrame.addView(mapView);

		mapView.addPOIItem(poi);
		mapView.setMapCenterPointAndZoomLevel(poi.getMapPoint(), 4, false);
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		teamInfo = state.getParcelable("teamInfo");
	}

	@Override
	public void onPause() {
		super.onPause();
		FrameLayout mapFrame = (FrameLayout) findViewById(R.id.frame_edit_team_profile_location_map);
		mapFrame.removeView(mapView);
		mapView = null;
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putParcelable("teamInfo", teamInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_team_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.action_edit_team:
			confirm();
			break;
		}

		return true;
	}

	private void confirm() {
		if (teamInfo.getImageUrl().startsWith("/"))
			ImageUtils.uploadImage(new DialogResponseHandler<UploadImageResponse>(DialogUtils.showWaitingDialog(this)) {

				@Override
				public void onReceiveOK(UploadImageResponse response) {
					teamInfo.setImageUrl(response.getPath());
					editTeamProfile();
				}

			}, teamInfo.getImageUrl());
		else
			editTeamProfile();

	}

	private void editTeamProfile() {
		GroundClient.editTeamProfile(new ResponseHandler<EditTeamProfileResponse>() {

			@Override
			public void onReceiveOK(EditTeamProfileResponse response) {
				finish();
			}

		}, teamInfo);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri image = data.getData();
			String[] projection = { Media.DATA };

			Cursor cursor = getContentResolver().query(image, projection, null, null, null);
			cursor.moveToFirst();

			int column = cursor.getColumnIndex(Media.DATA);
			teamInfo.setImageUrl(cursor.getString(column));

			cursor.close();
		} else if (requestCode == MapPointActivity.REQUEST_NEW_SPOT && resultCode == RESULT_OK && data != null) {
			teamInfo.setLatitude(data.getDoubleExtra(MapPointActivity.EXTRA_LATITUDE, 0));
			teamInfo.setLongitude(data.getDoubleExtra(MapPointActivity.EXTRA_LONGITUDE, 0));
			teamInfo.setAddress(data.getStringExtra(MapPointActivity.EXTRA_ADDRESS));

			TextView textTeamLocation = (TextView) findViewById(R.id.text_edit_team_profile_location);
			textTeamLocation.setText(teamInfo.getAddress());
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
