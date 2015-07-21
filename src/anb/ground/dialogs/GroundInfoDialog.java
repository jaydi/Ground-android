package anb.ground.dialogs;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.models.Ground;
import anb.ground.utils.UIScaleUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class GroundInfoDialog extends DialogFragment {
	private Ground ground;
	private GroundInfoDialogListener mListener;
	
	private FrameLayout locationMap;
	private MapView mapView;
	private MapPOIItem poi;

	public interface GroundInfoDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	public Ground getGround() {
		return ground;
	}

	public void setGround(Ground ground) {
		this.ground = ground;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (GroundInfoDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " class must implement ground info dialog listener");
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		locationMap.removeView(mapView);
		mapView = null;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		View view = inflater.inflate(R.layout.dialog_ground_info_layout, null);
		
		poi = new MapPOIItem();
		poi.setItemName(ground.getName());
		poi.setMapPoint(MapPoint.mapPointWithGeoCoord(ground.getLatitude(), ground.getLongitude()));
		poi.setMarkerType(MarkerType.BluePin);
		
		mapView = new MapView(getActivity());
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);
		mapView.setMapCenterPointAndZoomLevel(poi.getMapPoint(), 4, false);
		mapView.addPOIItem(poi);
		mapView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIScaleUtils.getPixels(150)));

		locationMap = (FrameLayout) view.findViewById(R.id.frame_ground_info_location_map);
		locationMap.addView(mapView);
		
		builder.setTitle(ground.getName())
		.setView(view)
		.setPositiveButton(R.string.select, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogPositiveClick(GroundInfoDialog.this);
			}
			
		})
		.setNegativeButton(R.string.cancel, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(GroundInfoDialog.this);
			}
			
		});
		
		
		return builder.create();
	}
}
