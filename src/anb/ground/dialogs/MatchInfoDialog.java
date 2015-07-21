package anb.ground.dialogs;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.MarkerType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.models.MatchInfo;
import anb.ground.protocols.MatchInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DateUtils;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.UIScaleUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchInfoDialog extends DialogFragment {
	private long matchId;
	private boolean managed;
	private MatchInfo match;
	private MatchInfoDialogListener listener;

	private MapView mapView;
	private MapPOIItem poi;
	private FrameLayout mapContainer;

	public interface MatchInfoDialogListener {
		public void onRequestMatchClick(long matchId, int homeTeamId);

		public void onCancelRequestMatchClick(DialogFragment dialog);
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public void setManaged(boolean managed) {
		this.managed = managed;
	}

	public void setListener(MatchInfoDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.dialog_match_info_layout, null, false);

		GroundClient.getMatchInfo(new ResponseHandler<MatchInfoResponse>() {

			@Override
			protected void onReceiveOK(MatchInfoResponse response) {
				match = response.getMatchInfo();
				setView(view);
			}

		}, matchId, 0);

		builder.setView(view);

		if (managed)
			builder.setPositiveButton(R.string.action_request, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeView();
					listener.onRequestMatchClick(match.getId(), match.getHomeTeamId());
				}

			}).setNegativeButton(R.string.cancel, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeView();
					listener.onCancelRequestMatchClick(MatchInfoDialog.this);
				}

			});
		else
			builder.setNeutralButton(R.string.close, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
				
			});

			return builder.create();
	}

	private void setView(View view) {
		ImageView imageOpponent = (ImageView) view.findViewById(R.id.image_match_info_opponent);
		TextView textOpponent = (TextView) view.findViewById(R.id.text_match_info_opponent);
		TextView textTime = (TextView) view.findViewById(R.id.text_match_info_time);
		TextView textLocation = (TextView) view.findViewById(R.id.text_match_info_location);

		ImageUtils.getThumnailImageCircular(imageOpponent, match.getHomeImageUrl());
		textOpponent.setText(match.getHomeTeamName());
		textTime.setText(DateUtils.getDateString(match.getStartTime()) + " " + DateUtils.getMatchTimeString(match.getStartTime(), match.getEndTime()));
		textLocation.setText(match.getGround().getName());

		poi = new MapPOIItem();
		poi.setItemName(match.getGround().getName());
		poi.setMapPoint(MapPoint.mapPointWithGeoCoord(match.getGround().getLatitude(), match.getGround().getLongitude()));
		poi.setMarkerType(MarkerType.BluePin);

		mapView = new MapView(getActivity());
		mapView.setDaumMapApiKey(GlobalApplication.MAP_API_KEY);
		mapView.setMapType(MapView.MapType.Standard);
		mapView.setMapCenterPointAndZoomLevel(poi.getMapPoint(), 4, false);
		mapView.addPOIItem(poi);
		mapView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIScaleUtils.getPixels(150)));

		mapContainer = (FrameLayout) view.findViewById(R.id.frame_simple_match_info_map);
		mapContainer.addView(mapView);
	}

	public void removeView() {
		if (mapView != null) {
			mapContainer.removeView(mapView);
			mapView = null;
		}
	}
}
