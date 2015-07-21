package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NewMatchOptionsDialog extends DialogFragment {
	private NewMatchOptionsDialogListener listener;
	
	public interface NewMatchOptionsDialogListener {
		public void onNewMatchOptionPicked(int position);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (NewMatchOptionsDialogListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(this.toString() + " must implement new match options dialog listener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(R.array.new_match_options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 2)
					getDialog().cancel();
				else
					listener.onNewMatchOptionPicked(which);
			}
			
		});
		
		return builder.create();
	}
}
