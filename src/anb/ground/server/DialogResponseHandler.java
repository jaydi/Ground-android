package anb.ground.server;

import anb.ground.protocols.DefaultResponse;
import android.app.Dialog;

public abstract class DialogResponseHandler<T extends DefaultResponse> extends ResponseHandler<T> {
	private Dialog dlg;

	public DialogResponseHandler(Dialog dlg) {
		this.dlg = dlg;
	}

	@Override
	protected void onReceiveResponse(T response) {
		if (dlg != null) {
			dlg.dismiss();
		}
		super.onReceiveResponse(response);
	}
}