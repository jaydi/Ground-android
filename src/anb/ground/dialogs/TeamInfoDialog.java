package anb.ground.dialogs;

import anb.ground.R;
import anb.ground.models.TeamInfo;
import anb.ground.utils.ImageUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamInfoDialog extends DialogFragment {
	private TeamInfo teamInfo;
	private boolean isRequest;
	private TeamInfoDialogListener mListener;

	public interface TeamInfoDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	public void setTeam(TeamInfo teamInfo) {
		this.teamInfo = teamInfo;
	}

	public void setIsRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mListener = (TeamInfoDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement team info dialog listener");
		}

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = inflater.inflate(R.layout.dialog_team_info_layout, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_team_info_picture);
		TextView textName = (TextView) view.findViewById(R.id.text_team_info_name);
		TextView textDescription = (TextView) view.findViewById(R.id.text_team_info_description);

		ImageUtils.getThumnailImageCircular(imageView, teamInfo.getImageUrl());
		textName.setText(teamInfo.getName());
		textDescription.setText(teamInfo.getAddress() + "\n\n연령 : " + teamInfo.getAvgBirth() + "\n팀원 : "
				+ teamInfo.getMembersCount() + "\n전적 : " + teamInfo.getWin() + " 승 " + teamInfo.getDraw() + " 무 " + teamInfo.getLose() + " 패");

		builder.setView(view).setPositiveButton((isRequest) ? R.string.select : R.string.join_team, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogPositiveClick(TeamInfoDialog.this);
			}
		}).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(TeamInfoDialog.this);
			}
		});

		return builder.create();
	}
}
