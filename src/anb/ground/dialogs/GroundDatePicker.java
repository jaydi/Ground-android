package anb.ground.dialogs;

import anb.ground.R;
import anb.ground.utils.DateUtils;
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

public class GroundDatePicker extends DialogFragment {
	private GroundDatePickerListener listener;

	public interface GroundDatePickerListener {
		public void onDateSet(int year, int month, int day);
	}
	
	public void setListener(GroundDatePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_date_picker_layout, null, false);

		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_date_picker_date);
		datePicker.setMinDate(DateUtils.getToday());
		
		builder.setView(view)
		.setPositiveButton(R.string.confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDateSet(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
			}
			
		})
		.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
			
		});

		return builder.create();
	}

}
