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

public class PhysicalPicker extends DialogFragment {
	private int height;
	private int weight;

	private PhysicalPickerListener listener;

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public interface PhysicalPickerListener {
		public void onPhysicalPicked(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (PhysicalPickerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement physical picker listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_physical_picker_layout, null, false);

		NumberPicker heightPicker = (NumberPicker) view.findViewById(R.id.number_physical_picker_height);
		NumberPicker weightPicker = (NumberPicker) view.findViewById(R.id.number_physical_picker_weight);

		heightPicker.setMinValue(80);
		heightPicker.setMaxValue(220);
		if (height != 0)
			heightPicker.setValue(height);
		else
			heightPicker.setValue(170);
		weightPicker.setMinValue(50);
		weightPicker.setMaxValue(150);
		if (weight != 0)
			weightPicker.setValue(weight);
		else
			weightPicker.setValue(70);

		builder.setView(view).setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onPhysicalPicked(PhysicalPicker.this);
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
