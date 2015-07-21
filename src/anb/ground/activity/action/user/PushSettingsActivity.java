package anb.ground.activity.action.user;

import anb.ground.R;
import anb.ground.activity.main.TrackedActivity;
import anb.ground.app.GlobalApplication;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PushSettingsActivity extends TrackedActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_settings);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
		
		setToggleButtons();
	}

	private void setToggleButtons() {
		ImageView togglePush = (ImageView) findViewById(R.id.image_push_settings_togglePush);
		ImageView togglePushToast = (ImageView) findViewById(R.id.image_push_settings_toggleToast);
		ImageView togglePushVibration = (ImageView) findViewById(R.id.image_push_settings_toggleVibration);
		
		if (GlobalApplication.pushSettings.isPushAllowed()) {
			enableOptions();
			togglePush.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_on));
		} else {
			disableOptions();
			togglePush.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_off));
		}
		
		if (GlobalApplication.pushSettings.isPushToastAllowed())
			togglePushToast.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_on));
		else
			togglePushToast.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_off));
		
		if (GlobalApplication.pushSettings.isPushVibrationAllowed())
			togglePushVibration.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_on));
		else
			togglePushVibration.setImageDrawable(getResources().getDrawable(R.drawable.button_toggle_off));
	}

	private void enableOptions() {
		LinearLayout itemPushToast = (LinearLayout) findViewById(R.id.linear_push_settings_toast);
		LinearLayout itemPushVibration = (LinearLayout) findViewById(R.id.linear_push_settings_vibration);
		
		itemPushToast.setVisibility(View.VISIBLE);
		itemPushVibration.setVisibility(View.VISIBLE);
	}

	private void disableOptions() {
		LinearLayout itemPushToast = (LinearLayout) findViewById(R.id.linear_push_settings_toast);
		LinearLayout itemPushVibration = (LinearLayout) findViewById(R.id.linear_push_settings_vibration);
		
		itemPushToast.setVisibility(View.GONE);
		itemPushVibration.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.push_settings, menu);
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

	@Override
	public void onPause() {
		super.onPause();
		GlobalApplication.getInstance().savePushSettings();
	}

	public void togglePushAllowance(View view) {
		if (GlobalApplication.pushSettings.isPushAllowed())
			GlobalApplication.pushSettings.setPushAllowed(false);
		else
			GlobalApplication.pushSettings.setPushAllowed(true);
		
		setToggleButtons();
	}

	public void togglePushToastAllowance(View view) {
		if (GlobalApplication.pushSettings.isPushToastAllowed())
			GlobalApplication.pushSettings.setPushToastAllowed(false);
		else
			GlobalApplication.pushSettings.setPushToastAllowed(true);
		
		setToggleButtons();
	}

	public void togglePushVibrationAllowance(View view) {
		if (GlobalApplication.pushSettings.isPushVibrationAllowed())
			GlobalApplication.pushSettings.setPushVibrationAllowed(false);
		else
			GlobalApplication.pushSettings.setPushVibrationAllowed(true);
		
		setToggleButtons();
	}

}
