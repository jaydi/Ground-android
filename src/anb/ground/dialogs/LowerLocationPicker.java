package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LowerLocationPicker extends DialogFragment {
	private LowerLocationPickerListener listener;

	public interface LowerLocationPickerListener {
		public void onLowerLocationPicked(int arraySrc, int position);
	}

	private int position;

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (LowerLocationPickerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement lower location picker listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setItems(getArraySrc(position), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onLowerLocationPicked(getArraySrc(position), which);
			}

		});

		return builder.create();
	}

	private int getArraySrc(int position) {
		switch (position) {
		case 0:
			return R.array.locations2_0;
		case 1:
			return R.array.locations2_1;
		case 2:
			return R.array.locations2_2;
		default:
			return R.array.locations2_0;
		}
	}
}
