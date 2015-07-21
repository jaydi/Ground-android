package anb.ground.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	public static void showConfirmDialogFromActionBar(Context context, DialogInterface.OnClickListener yesCallback, DialogInterface.OnClickListener noCallback,
			String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setPositiveButton(android.R.string.yes, yesCallback);
		builder.setNegativeButton(android.R.string.no, noCallback);
		builder.create().show();
	}
	
	public static void showConfirmDialogFromActionBar(Context context, DialogInterface.OnClickListener yesCallback, DialogInterface.OnClickListener noCallback,
			int msgId) {
		String msg = context.getResources().getString(msgId);
		showConfirmDialogFromActionBar(context, yesCallback, noCallback, msg);
	}

	public static void showConfirmDialogFromActionBar(Context context, DialogInterface.OnClickListener yesCallback, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setPositiveButton(android.R.string.yes, yesCallback);
		builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

	public static ProgressDialog showWaitingDialog(Context context) {
		ProgressDialog progressDlg = new ProgressDialog(context);
		progressDlg.setMessage("잠시만 기다려주세요");
		progressDlg.setCancelable(true);
		progressDlg.show();
		return progressDlg;
	}
}
