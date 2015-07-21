package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PictureOptionsDialog extends DialogFragment {
	private PictureOptionDialogFragmentListener listener;

	public interface PictureOptionDialogFragmentListener {
		public void onPictureOptionClick(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			listener = (PictureOptionDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement picture option dialog fragment listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setItems(R.array.picture_options, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 2)
					getDialog().cancel();
				else
					listener.onPictureOptionClick(which);
			}

		});

		return builder.create();
	}
}
