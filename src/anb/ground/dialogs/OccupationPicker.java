package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class OccupationPicker extends DialogFragment {
	private OccupationPickerListener listener;
	
	public interface OccupationPickerListener {
		public void onOccupationPicked(int position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (OccupationPickerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement occupation picker listener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setItems(R.array.occupations, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onOccupationPicked(which);
			}
			
		});
		
		return builder.create();
	}
}
