package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LocationPicker extends DialogFragment {
	private LocationPickerListener listener;
	
	public interface LocationPickerListener {
		public void onLocationPicked(int which);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (LocationPickerListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement location picker listener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setItems(R.array.locations1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onLocationPicked(which);
			}
			
		});
		
		return builder.create();
	}
}
