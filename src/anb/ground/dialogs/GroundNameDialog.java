package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class GroundNameDialog extends DialogFragment {
	private GroundNameDialogListener mListener;
	
	public interface GroundNameDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (GroundNameDialogListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ground name dialog listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_grond_name_title)
		.setView(inflater.inflate(R.layout.dialog_text_input_layout, null))
		.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogPositiveClick(GroundNameDialog.this);
			}
		})
		.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mListener.onDialogNegativeClick(GroundNameDialog.this);
			}
		});
		
		return builder.create();
	}
	
}
