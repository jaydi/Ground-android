package anb.ground.dialogs;

import anb.ground.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class ScorePicker extends DialogFragment {
	private ScorePickerDialogListener listener;

	private int homeScore;
	private int awayScore;

	public interface ScorePickerDialogListener {
		public void onPositiveClick(DialogFragment dialog);
		public void onNegativeClick(DialogFragment dialog);
	}

	public void setScores(int homeScore, int awayScore) {
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (ScorePickerDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement score picker dialog listener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = inflater.inflate(R.layout.dialog_score_picker_layout, null);

		NumberPicker homeScorePicker = (NumberPicker) view.findViewById(R.id.number_score_picker_home);
		NumberPicker awayScorePicker = (NumberPicker) view.findViewById(R.id.number_score_picker_away);

		homeScorePicker.setMinValue(0);
		homeScorePicker.setMaxValue(20);
		homeScorePicker.setValue(homeScore);
		awayScorePicker.setMinValue(0);
		awayScorePicker.setMaxValue(20);
		awayScorePicker.setValue(awayScore);

		builder.setView(view)
		.setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onPositiveClick(ScorePicker.this);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onNegativeClick(ScorePicker.this);
			}
		});

		return builder.create();
	}
}
