package anb.ground.activity.action.user;

import java.util.List;

import anb.ground.R;
import anb.ground.activity.main.TrackedFragmentActivity;
import anb.ground.activity.main.user.UserMainActivity;
import anb.ground.app.GlobalApplication;
import anb.ground.dialogs.LocationPicker;
import anb.ground.dialogs.LocationPicker.LocationPickerListener;
import anb.ground.dialogs.LowerLocationPicker;
import anb.ground.dialogs.LowerLocationPicker.LowerLocationPickerListener;
import anb.ground.dialogs.MainFootPicker;
import anb.ground.dialogs.MainFootPicker.MainFootPickerListener;
import anb.ground.dialogs.NumberPickerDialog;
import anb.ground.dialogs.NumberPickerDialog.NumberPickerFragmentListener;
import anb.ground.dialogs.OccupationPicker;
import anb.ground.dialogs.OccupationPicker.OccupationPickerListener;
import anb.ground.dialogs.PhysicalPicker;
import anb.ground.dialogs.PhysicalPicker.PhysicalPickerListener;
import anb.ground.dialogs.PictureOptionsDialog;
import anb.ground.dialogs.PictureOptionsDialog.PictureOptionDialogFragmentListener;
import anb.ground.dialogs.PositionPicker;
import anb.ground.dialogs.PositionPicker.PositionPickerListener;
import anb.ground.models.LocalUser;
import anb.ground.models.User;
import anb.ground.models.UserInfo;
import anb.ground.protocols.EditProfileResponse;
import anb.ground.protocols.RegisterResponse;
import anb.ground.protocols.UploadImageResponse;
import anb.ground.server.DialogResponseHandler;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import anb.ground.utils.DateUtils;
import anb.ground.utils.DialogUtils;
import anb.ground.utils.Encryption;
import anb.ground.utils.ImageUtils;
import anb.ground.utils.Validator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class EditProfileActivity extends TrackedFragmentActivity implements PictureOptionDialogFragmentListener, NumberPickerFragmentListener, PositionPickerListener,
		PhysicalPickerListener, MainFootPickerListener, OccupationPickerListener, LocationPickerListener, LowerLocationPickerListener {
	public final static String EXTRA_PROFILE = "anb.ground.extra.profile";
	public final static String EXTRA_IS_FROM_JOIN = "anb.ground.extra.isFromJoin";
	public final static int REQUEST_GET_IMAGE = 48787;

	private String email;
	private String pswd;

	private UserInfo userInfo;
	private String profileImageUrl;
	private String name;
	private String phoneNumber;

	private int birthYear;
	private int height;
	private int weight;
	private int position = -1;
	private int mainFoot = -1;
	private String location1;
	private String location2;
	private int occupation = -1;

	private boolean isFromJoin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		isFromJoin = getIntent().getBooleanExtra(EXTRA_IS_FROM_JOIN, false);
		if (isFromJoin) {
			getActionBar().hide();
			email = getIntent().getStringExtra(User.EXTRA_USER_EMAIL);
			pswd = getIntent().getStringExtra(User.EXTRA_USER_PSWD);
		} else {
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setLogo(getResources().getDrawable(R.drawable.menu_back));
		}

		userInfo = getIntent().getParcelableExtra(UserInfo.EXTRA_USER_INFO);
		if (userInfo != null)
			setParameters(userInfo);
	}

	private void setParameters(UserInfo userInfo) {
		profileImageUrl = userInfo.getProfileImageUrl();
		phoneNumber = userInfo.getPhoneNumber();
		name = userInfo.getName();
		birthYear = userInfo.getBirthYear();
		position = userInfo.getPosition();
		height = userInfo.getHeight();
		weight = userInfo.getWeight();
		mainFoot = userInfo.getMainFoot();
		location1 = userInfo.getLocation1();
		location2 = userInfo.getLocation2();
		occupation = userInfo.getOccupation();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTexts();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		EditText editName = (EditText) findViewById(R.id.edit_text_edit_profile_name);
		EditText editPhone = (EditText) findViewById(R.id.edit_text_edit_profile_phone);
		name = editName.getEditableText().toString();
		phoneNumber = editPhone.getEditableText().toString();
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("name", name);
		state.putString("phoneNumber", phoneNumber);
		state.putString("profileImageUrl", profileImageUrl);
		state.putInt("birthYear", birthYear);
		state.putInt("position", position);
		state.putInt("height", height);
		state.putInt("weight", weight);
		state.putInt("mainFoot", mainFoot);
		state.putString("location1", location1);
		state.putString("location2", location2);
		state.putInt("occupation", occupation);
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		name = state.getString("name");
		phoneNumber = state.getString("phoneNumber");
		profileImageUrl = state.getString("profileImageUrl");
		birthYear = state.getInt("birthYear");
		position = state.getInt("position");
		height = state.getInt("height");
		weight = state.getInt("weight");
		mainFoot = state.getInt("mainFoot");
		location1 = state.getString("location1");
		location2 = state.getString("location2");
		occupation = state.getInt("occupation");
	}

	private void setTexts() {
		ImageView imageView = (ImageView) findViewById(R.id.image_edit_profile_picture);
		EditText editName = (EditText) findViewById(R.id.edit_text_edit_profile_name);
		EditText editPhone = (EditText) findViewById(R.id.edit_text_edit_profile_phone);
		TextView textBirthYear = (TextView) findViewById(R.id.text_edit_profile_birth_year);
		TextView textPosition = (TextView) findViewById(R.id.text_edit_profile_position);
		TextView textPhysical = (TextView) findViewById(R.id.text_edit_profile_physical);
		TextView textMainFoot = (TextView) findViewById(R.id.text_edit_profile_main_foot);
		TextView textLocation = (TextView) findViewById(R.id.text_edit_profile_location);
		TextView textOccupation = (TextView) findViewById(R.id.text_edit_profile_occupation);

		ImageUtils.getThumnailImage(imageView, profileImageUrl);
		editName.setText(name);
		editPhone.setText(phoneNumber);
		if (birthYear != 0)
			textBirthYear.setText("" + birthYear);
		if (position != -1)
			textPosition.setText(getResources().getStringArray(R.array.positions)[position]);
		if (height != 0 && weight != 0)
			textPhysical.setText("" + height + "cm" + " / " + weight + "kg");
		if (mainFoot != -1)
			textMainFoot.setText(getResources().getStringArray(R.array.main_foot)[mainFoot]);
		if (!Validator.isEmpty(location1) && !Validator.isEmpty(location2))
			textLocation.setText(location1 + " " + location2);
		if (occupation != -1)
			textOccupation.setText(getResources().getStringArray(R.array.occupations)[occupation]);
	}

	public void getImage(View view) {
		PictureOptionsDialog dialog = new PictureOptionsDialog();
		dialog.show(getSupportFragmentManager(), "getImage");
	}

	@Override
	public void onPictureOptionClick(int position) {
		Intent intent;

		if (position == 0) {
			intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		} else {
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		}

		PackageManager manager = getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(intent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe)
			startActivityForResult(intent, REQUEST_GET_IMAGE);
	}

	public void getBirthYear(View view) {
		NumberPickerDialog picker = new NumberPickerDialog();
		picker.setMin(DateUtils.currentYear - 70);
		picker.setMax(DateUtils.currentYear - 10);
		picker.show(getSupportFragmentManager(), "birthYearPicker");
	}

	@Override
	public void onNumberPicked(DialogFragment dialog) {
		NumberPicker picker = (NumberPicker) dialog.getDialog().findViewById(R.id.number_picker_picker);
		TextView textBirthYear = (TextView) findViewById(R.id.text_edit_profile_birth_year);

		birthYear = picker.getValue();
		textBirthYear.setText("" + birthYear);
	}

	public void getPosition(View view) {
		PositionPicker picker = new PositionPicker();
		picker.show(getSupportFragmentManager(), "positionPicker");
	}

	@Override
	public void onPositionPicked(int position) {
		TextView textPosition = (TextView) findViewById(R.id.text_edit_profile_position);

		this.position = position;
		textPosition.setText(getResources().getStringArray(R.array.positions)[position]);
	}

	public void getPhysical(View view) {
		PhysicalPicker picker = new PhysicalPicker();
		picker.show(getSupportFragmentManager(), "physicalPicker");
	}

	@Override
	public void onPhysicalPicked(DialogFragment dialog) {
		NumberPicker heightPicker = (NumberPicker) dialog.getDialog().findViewById(R.id.number_physical_picker_height);
		NumberPicker weightPicker = (NumberPicker) dialog.getDialog().findViewById(R.id.number_physical_picker_weight);

		height = heightPicker.getValue();
		weight = weightPicker.getValue();

		TextView textPhysical = (TextView) findViewById(R.id.text_edit_profile_physical);
		textPhysical.setText("" + height + "cm" + " / " + weight + "kg");
	}

	public void getMainFoot(View view) {
		MainFootPicker picker = new MainFootPicker();
		picker.show(getSupportFragmentManager(), "mainFootPicker");
	}

	@Override
	public void onMainFootPicked(int position) {
		TextView textMainFoot = (TextView) findViewById(R.id.text_edit_profile_main_foot);

		mainFoot = position;
		textMainFoot.setText(getResources().getStringArray(R.array.main_foot)[mainFoot]);
	}

	public void getLocation(View view) {
		LocationPicker picker = new LocationPicker();
		picker.show(getSupportFragmentManager(), "locationPicker");
	}

	@Override
	public void onLocationPicked(int position) {
		location1 = getResources().getStringArray(R.array.locations1)[position];
		getLowerLocation(position);
	}

	private void getLowerLocation(int position) {
		LowerLocationPicker picker = new LowerLocationPicker();
		picker.setPosition(position);
		picker.show(getSupportFragmentManager(), "lowerLocationPicker");
	}

	@Override
	public void onLowerLocationPicked(int arraySrc, int position) {
		location2 = getResources().getStringArray(arraySrc)[position];

		TextView textLocation = (TextView) findViewById(R.id.text_edit_profile_location);
		textLocation.setText(location1 + " " + location2);
	}

	public void getOccupation(View view) {
		OccupationPicker picker = new OccupationPicker();
		picker.show(getSupportFragmentManager(), "occupationPicker");
	}

	@Override
	public void onOccupationPicked(int position) {
		occupation = position;

		TextView textOccupation = (TextView) findViewById(R.id.text_edit_profile_occupation);
		textOccupation.setText(getResources().getStringArray(R.array.occupations)[occupation]);
	}

	public void confirm(View view) {
		if (isFromJoin)
			addUser();
		else
			editProfile();
	}

	private void addUser() {
		String regId = GlobalApplication.regId;
		int appVer = GlobalApplication.appVer;
		String uuid = GlobalApplication.uuid;
		
		GroundClient.register(new DialogResponseHandler<RegisterResponse>(DialogUtils.showWaitingDialog(this)) {

			@Override
			protected void onReceiveOK(RegisterResponse response) {
				String sessionKey = response.getSessionKey();

				if (!Validator.validateSessionKey(sessionKey))
					return;

				User user = User.newInstance(response.getUserId(), email, null);
				LocalUser.getInstance().setUser(user);
				LocalUser.getInstance().setSessionKey(sessionKey);
				LocalUser.log();
				
				editProfile();
			}

		}, email, Encryption.encrypt(pswd), regId, 0, appVer, uuid);
	}

	private void editProfile() {
		EditText editName = (EditText) findViewById(R.id.edit_text_edit_profile_name);
		EditText editPhone = (EditText) findViewById(R.id.edit_text_edit_profile_phone);

		name = editName.getEditableText().toString();
		phoneNumber = editPhone.getEditableText().toString();

		UserInfo profile = new UserInfo(name, birthYear, position, height, weight, mainFoot, location1, location2, occupation, phoneNumber, profileImageUrl);

		if (Validator.validateProfile(profile))
			if (!Validator.isEmpty(profileImageUrl) && profileImageUrl.startsWith("/"))
				sendWithImage(profile);
			else
				send(profile);

	}

	private void sendWithImage(final UserInfo profile) {
		ImageUtils.uploadImage(new DialogResponseHandler<UploadImageResponse>(DialogUtils.showWaitingDialog(this)) {

			@Override
			public void onReceiveOK(UploadImageResponse response) {
				profile.setProfileImageUrl(response.getPath());
				send(profile);
			}

		}, profileImageUrl);
	}

	private void send(final UserInfo profile) {
		GroundClient.editProfile(new ResponseHandler<EditProfileResponse>() {

			@Override
			public void onReceiveOK(EditProfileResponse response) {
				LocalUser.getInstance().getUser().setName(profile.getName());
				LocalUser.getInstance().getUser().setImageUrl(profile.getProfileImageUrl());

				Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear history
				startActivity(intent);
				finish();
			}

		}, profile);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri image = data.getData();
			String[] projection = { Media.DATA };

			Cursor cursor = getContentResolver().query(image, projection, null, null, null);
			cursor.moveToFirst();

			int column = cursor.getColumnIndex(Media.DATA);
			profileImageUrl = cursor.getString(column);

			cursor.close();
		}
	}
}
