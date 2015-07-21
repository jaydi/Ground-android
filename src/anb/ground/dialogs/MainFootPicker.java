package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MainFootPicker extends DialogFragment {
	private MainFootPickerListener listener;
	
	public interface MainFootPickerListener {
		public void onMainFootPicked(int position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (MainFootPickerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement main foot picker listener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setItems(R.array.main_foot, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onMainFootPicked(which);
			}
			
		});
		
		return builder.create();
	}
}
