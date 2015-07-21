package anb.ground.server;

import anb.ground.models.LocalUser;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.DefaultResponse.StatusCode;
import anb.ground.utils.ToastUtils;
import android.os.Handler;
import android.os.Message;

public abstract class ResponseHandler<T extends DefaultResponse> extends Handler {
	protected void onReceiveResponse(T response) {
		if (response.isOK())
			onReceiveOK(response);
		else
			onReceiveError(response);
	}

	protected abstract void onReceiveOK(T response);

	protected void onReceiveError(T response) {
		if (response.getStatusCode() == StatusCode.INVALID_SESSION_KEY)
			LocalUser.getInstance().logout();
		
		ToastUtils.showServerErrorMessage(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final void handleMessage(Message msg) {
		onReceiveResponse((T) msg.obj);
	}

}
