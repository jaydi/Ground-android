package anb.ground.activity.starting;

import java.util.Arrays;

import anb.ground.R;
import anb.ground.activity.action.user.EditProfileActivity;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.LocalUser;
import anb.ground.models.User;
import anb.ground.models.UserInfo;
import anb.ground.protocols.FacebookLoginResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ToastUtils;
import anb.ground.utils.Validator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class StartingActivity extends TrackedActivity {
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private LoginButton facebookLogin;
	private UiLifecycleHelper uiHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting);

		facebookLogin = (LoginButton) findViewById(R.id.facebook_login_starting);
		facebookLogin.setReadPermissions(Arrays.asList("email"));
		facebookLogin.setBackgroundResource(R.drawable.ic_facebook_start);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		
		GlobalApplication.getInstance().initGCM();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void checkSession(Session session, SessionState state, Exception exception) {
		if (session.isOpened())
			login(session);
	}

	private void login(final Session session) {
		final String regId = GlobalApplication.regId;
		final int appVer = GlobalApplication.appVer;
		final String uuid = GlobalApplication.uuid;
		final Dialog dialog = DialogUtils.showWaitingDialog(this);

		Request.executeMeRequestAsync(session, new GraphUserCallback() {

			@Override
			public void onCompleted(final GraphUser user, Response response) {
				if (user != null) {
					final String imageUrl = "http://graph.facebook.com/" + user.getId() + "/picture?style=small";

					GroundClient.facebookLogin(new ResponseHandler<FacebookLoginResponse>() {

						@Override
						protected void onReceiveOK(FacebookLoginResponse response) {
							String sessionKey = response.getSessionKey();
							if (Validator.isEmpty(sessionKey))
								return;

							User luser = User.newInstance(response.getUserId(), user.getName(), imageUrl);
							LocalUser.getInstance().setUser(luser);
							LocalUser.getInstance().setSessionKey(sessionKey);
							LocalUser.log();

							if (response.isFirstLogin()) {
								UserInfo userInfo = new UserInfo();
								userInfo.setProfileImageUrl(imageUrl);
								userInfo.setName(user.getName());
								
								Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
								intent.putExtra(UserInfo.EXTRA_USER_INFO, userInfo);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear history
								startActivity(intent);
							} else {
								Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear history
								startActivity(intent);
							}
							
							dialog.dismiss();
							finish();
						}

					}, user.getProperty("email").toString(), user.getName(), imageUrl, regId, 0, appVer, uuid);
				} else {
					ToastUtils.show("graph user is null");
					dialog.dismiss();
				}
			}

		});

	}

	public void onClickStartByFacebook(View view) {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	public void onClickStartByEmail(View view) {
		Intent intent = new Intent(this, StartByEmailActivity.class);
		startActivity(intent);
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			checkSession(session, state, exception);
		}
	}
}
