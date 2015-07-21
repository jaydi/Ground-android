package anb.ground.dialogs;

import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.models.UserInfo;
import anb.ground.protocols.UserInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.StringProvider;
import anb.ground.utils.Validator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberShowDialog extends DialogFragment {
	private int userId;
	private boolean isWannabe;
	private UserInfo memberInfo;

	private MemberShowDialogListener listener;

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setIsWannabe(boolean isWannabe) {
		this.isWannabe = isWannabe;
	}

	public interface MemberShowDialogListener {
		public void onMemberShowPositiveClicked(int memberId);

		public void onMemberShowNegativeClicked(int memberId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (MemberShowDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement member show dialog listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_user_info_layout, null, false);

		GroundClient.getUserInfo(new ResponseHandler<UserInfoResponse>() {

			@Override
			public void onReceiveOK(UserInfoResponse response) {
				memberInfo = response.getUserInfo();
				setView(view);
			}

		}, userId);

		builder.setView(view);

		if (isWannabe) {
			builder.setPositiveButton(R.string.accept, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					listener.onMemberShowPositiveClicked(userId);
				}

			}).setNeutralButton(R.string.later, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDialog().cancel();
				}

			}).setNegativeButton(R.string.reject, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					listener.onMemberShowNegativeClicked(userId);
				}

			});
		} else {
			builder.setNeutralButton(R.string.close, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getDialog().cancel();
				}

			});
		}

		return builder.create();
	}

	private void setView(View view) {
		ImageView imageView = (ImageView) view.findViewById(R.id.image_user_info_picture);
		TextView textName = (TextView) view.findViewById(R.id.text_user_info_name);
		TextView textPhone = (TextView) view.findViewById(R.id.text_user_info_phone);
		TextView textBirth = (TextView) view.findViewById(R.id.text_user_info_birth);
		TextView textPosition = (TextView) view.findViewById(R.id.text_user_info_position);
		TextView textPhysical = (TextView) view.findViewById(R.id.text_user_info_physical);
		TextView textMainFoot = (TextView) view.findViewById(R.id.text_user_info_mainfoot);
		TextView textLocation = (TextView) view.findViewById(R.id.text_user_info_location);
		TextView textOccupation = (TextView) view.findViewById(R.id.text_user_info_occupation);

		ImageUtils.getThumnailImage(imageView, memberInfo.getProfileImageUrl());
		textName.setText(memberInfo.getName());
		
		if (isWannabe)
			textPhone.setVisibility(View.GONE);

		if (Validator.isEmpty(memberInfo.getPhoneNumber()))
			textPhone.setText(StringProvider.getString(R.string.empty_phonenumber));
		else
			textPhone.setText(memberInfo.getPhoneNumber());

		if (memberInfo.getBirthYear() == 0)
			textBirth.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textBirth.setText("" + memberInfo.getBirthYear());

		if (memberInfo.getPosition() == -1)
			textPosition.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textPosition.setText(GlobalApplication.getInstance().getResources().getStringArray(R.array.positions)[memberInfo.getPosition()]);

		if (memberInfo.getWeight() == 0 || memberInfo.getHeight() == 0)
			textPhysical.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textPosition.setText(memberInfo.getHeight() + "cm / " + memberInfo.getWeight() + "kg");

		if (memberInfo.getMainFoot() == -1)
			textMainFoot.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textMainFoot.setText(GlobalApplication.getInstance().getResources().getStringArray(R.array.main_foot)[memberInfo.getMainFoot()]);

		if (Validator.isEmpty(memberInfo.getLocation1()))
			textLocation.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textLocation.setText(memberInfo.getLocation1() + " " + memberInfo.getLocation2());

		if (memberInfo.getOccupation() == -1)
			textOccupation.append(" " + StringProvider.getString(R.string.empty_info));
		else
			textOccupation.setText(GlobalApplication.getInstance().getResources().getStringArray(R.array.occupations)[memberInfo.getOccupation()]);
	}
}
