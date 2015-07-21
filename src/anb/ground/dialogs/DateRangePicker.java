package anb.ground.dialogs;

import anb.ground.R;
import anb.ground.utils.DateUtils;
import anb.ground.utils.Validator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

public class DateRangePicker extends DialogFragment {
	private DateRangePickerListener listener;

	public interface DateRangePickerListener {
		public void onDatePicked(int[] numbers);
	}

	public void setListener(DateRangePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle state) {
		super.onCreateDialog(state);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_date_range_picker_layout, null, false);

		final DatePicker startDatePicker = (DatePicker) view.findViewById(R.id.date_date_range_picker_start);
		final DatePicker endDatePicker = (DatePicker) view.findViewById(R.id.date_date_range_picker_end);

		startDatePicker.setMinDate(DateUtils.getToday());
		endDatePicker.setMinDate(DateUtils.getToday());

		builder.setView(view).setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int[] numbers = new int[] { startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(), endDatePicker.getYear(),
						endDatePicker.getMonth(), endDatePicker.getDayOfMonth() };
				
				if(Validator.validateDateRange(numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5]))
					listener.onDatePicked(numbers);
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
