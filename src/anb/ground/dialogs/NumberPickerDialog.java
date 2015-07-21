package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerDialog extends DialogFragment {
	private NumberPickerFragmentListener listener;

	private int min;
	private int max;

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public interface NumberPickerFragmentListener {
		public void onNumberPicked(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (NumberPickerFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement number picker fragment Exception");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_number_picker_layout, null, false);

		NumberPicker picker = (NumberPicker) view.findViewById(R.id.number_picker_picker);
		picker.setMinValue(min);
		picker.setMaxValue(max);
		picker.setValue(max - 20);
		
		builder.setView(view).setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onNumberPicked(NumberPickerDialog.this);
			}

		}).setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getDialog().cancel();
			}

		});

		return builder.create();
	}
}
