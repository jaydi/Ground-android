package anb.ground.dialogs;

import anb.ground.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class TimeRangePicker extends DialogFragment {
	private TimeRangePickerListener listener;

	public interface TimeRangePickerListener {
		public void onTimeRangePicked(int hour, int min);
	}

	public void setListener(TimeRangePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle state) {
		super.onCreateDialog(state);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_time_range_picker_layout, null, false);

		final NumberPicker hourPicker = (NumberPicker) view.findViewById(R.id.number_time_hour_picker_start);
		final NumberPicker minPicker = (NumberPicker) view.findViewById(R.id.number_time_min_picker_start);

		String[] minuteValues = new String[6];
		for (int i = 0; i < minuteValues.length; i++) {
			String value = String.valueOf(i * 10);
			minuteValues[i] = (value.length() < 2) ? "0" + value : value;
		}

		hourPicker.setMinValue(0);
		hourPicker.setMaxValue(23);
		hourPicker.setValue(0);

		minPicker.setMinValue(0);
		minPicker.setMaxValue(59);
		minPicker.setDisplayedValues(minuteValues);
		minPicker.setValue(0);

		builder.setView(view).setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int hour = hourPicker.getValue();
				int min = minPicker.getValue();

				listener.onTimeRangePicked(hour, min);
			}

		}).setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}

		});

		return builder.create();
	}
}
