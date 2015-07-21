package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.action.user.EditProfileActivity;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.LocalUser;
import anb.ground.models.User;
import anb.ground.protocols.LoginResponse;
import anb.ground.protocols.ValidateEmailResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.Encryption;
import anb.ground.utils.Validator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class StartByEmailActivity extends TrackedFragmentActivity {
	private EditText editTextEmail, editTextPassword, editTextConfirmPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_by_email);
		setFonts();

		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

			LoginFragment loginFragment = new LoginFragment();
			SignUpFragment signUpFragment = new SignUpFragment();

			fragmentTransaction.add(R.id.frame_start_by_email_login, loginFragment);
			fragmentTransaction.add(R.id.frame_start_by_email_signup, signUpFragment);
			fragmentTransaction.commit();
		}
	}

	private void setFonts() {
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
		GlobalApplication.setAppFont(mContainer);
	}

	public void showLogin(View view) {
		changeFragment(true);
	}

	public void showSignUp(View view) {
		changeFragment(false);
	}

	private void changeFragment(boolean position) {
		if (position) {
			findViewById(R.id.frame_start_by_email_login).setVisibility(View.VISIBLE);
			findViewById(R.id.frame_start_by_email_signup).setVisibility(View.GONE);
			findViewById(R.id.button_start_by_email_login).setBackgroundResource(R.drawable.background_tab_selected);
			findViewById(R.id.button_start_by_email_signup).setBackgroundResource(R.drawable.background_tab_not_selected);
		} else {
			findViewById(R.id.frame_start_by_email_login).setVisibility(View.GONE);
			findViewById(R.id.frame_start_by_email_signup).setVisibility(View.VISIBLE);
			findViewById(R.id.button_start_by_email_login).setBackgroundResource(R.drawable.background_tab_not_selected);
			findViewById(R.id.button_start_by_email_signup).setBackgroundResource(R.drawable.background_tab_selected);
		}
	}

	public void clickLogin(View view) {
		String regId = GlobalApplication.regId;
		int appVer = GlobalApplication.appVer;
		String uuid = GlobalApplication.uuid;

		editTextEmail = (EditText) findViewById(R.id.edit_login_email);
		editTextPassword = (EditText) findViewById(R.id.edit_login_password);

		String email = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();

		if (Validator.validateLogin(email, password))
			GroundClient.login(new DialogResponseHandler<LoginResponse>(DialogUtils.showWaitingDialog(this)) {

				@Override
				protected void onReceiveOK(LoginResponse response) {
					String sessionKey = response.getSessionKey();
					if (!Validator.validateSessionKey(sessionKey))
						return;

					User user = User.newInstance(response.getUserId(), response.getName(), response.getImageUrl());
					LocalUser.getInstance().setUser(user);
					LocalUser.getInstance().setSessionKey(sessionKey);
					LocalUser.log();

					Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear history
					startActivity(intent);
				}

			}, email, Encryption.encrypt(password), regId, 0, appVer, uuid);
	}

	public void nextStep(View view) {
		editTextEmail = (EditText) findViewById(R.id.edit_sign_up_email);
		editTextPassword = (EditText) findViewById(R.id.edit_sign_up_password);
		editTextConfirmPassword = (EditText) findViewById(R.id.edit_sign_up_confirm);

		final String email = editTextEmail.getText().toString();
		final String password = editTextPassword.getText().toString();
		String confirmPassword = editTextConfirmPassword.getText().toString();

		if (Validator.validateRegister(email, password, confirmPassword)) {
			GroundClient.validateEmail(new ResponseHandler<ValidateEmailResponse>() {

				@Override
				protected void onReceiveOK(ValidateEmailResponse response) {
					if (response.isValidEmail()) {
						Intent intent = new Intent(StartByEmailActivity.this, EditProfileActivity.class);
						intent.putExtra(EditProfileActivity.EXTRA_IS_FROM_JOIN, true);
						intent.putExtra(User.EXTRA_USER_EMAIL, email);
						intent.putExtra(User.EXTRA_USER_PSWD, password);
						startActivity(intent);
					}
				}

			}, email);

		}
	}

	public void forgotUserInfo(View view) {
		Intent intent = new Intent(this, NewPswdActivity.class);
		startActivity(intent);
	}
}
