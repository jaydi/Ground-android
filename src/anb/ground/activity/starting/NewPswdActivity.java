package anb.ground.activity.starting;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.protocols.NewPswdResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.ToastUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewPswdActivity extends TrackedActivity {
	EditText editTextEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_pswd);
	}

	public void clickSend(View view) {
		editTextEmail = (EditText) findViewById(R.id.edit_sign_up_email);
		String email = editTextEmail.getText().toString();

		GroundClient.sendNewPswd(new DialogResponseHandler<NewPswdResponse>(DialogUtils.showWaitingDialog(this)) {
			
			@Override
			protected void onReceiveOK(NewPswdResponse response) {
				ToastUtils.show(R.string.new_pswd_sent);

				Intent intent = new Intent(getApplicationContext(), StartByEmailActivity.class);
				startActivity(intent);
			}
			
		}, email);
	}

}
