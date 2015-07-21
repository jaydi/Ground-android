package anb.ground.activity.action.user;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.models.LocalUser;
import anb.ground.models.UserInfo;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.UserInfoResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends TrackedActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			break;
		}

		return true;
	}

	public void editProfile(View view) {
		GroundClient.getUserInfo(new ResponseHandler<UserInfoResponse>() {

			@Override
			protected void onReceiveOK(UserInfoResponse response) {
				Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
				intent.putExtra(UserInfo.EXTRA_USER_INFO, response.getUserInfo());
				startActivity(intent);
			}

		}, LocalUser.getInstance().getUser().getId());
	}
	
	public void feedSettings(View view) {
		// TODO
	}
	
	public void pushSettings(View view) {
		Intent intent = new Intent(this, PushSettingsActivity.class);
		startActivity(intent);
	}

	public void logout(View view) {
		GroundClient.logout(new ResponseHandler<DefaultResponse>() {

			@Override
			protected void onReceiveOK(DefaultResponse response) {
			}
			
		}, GlobalApplication.uuid);
		LocalUser.getInstance().logout();
	}
}
