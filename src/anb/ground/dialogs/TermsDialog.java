package anb.ground.dialogs;

import anb.ground.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class TermsDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.simple_text_layout, null, false);
		
		TextView text = (TextView) view.findViewById(R.id.text_simple_text);
		text.setText("terms");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view).setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		return builder.create();
	}
}
