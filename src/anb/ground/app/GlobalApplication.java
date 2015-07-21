package anb.ground.app;

import java.io.IOException;

import anb.ground.activity.starting.StartingActivity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GlobalApplication extends Application {
	private static GlobalApplication instance = null;

	//API KEYS
	public static final String MAP_API_KEY = "d616f264bd2d7eb9513b9f22b0b4b8d9";
	public static final String LOCAL_API_KEY = "4b72b79b3c305b63e82c791d88276c51adcf84af";
	
	// GCM PREFERENCES
	private static final String PROPERTY_REG_ID = "anb.ground.property.regId";
	private static final String PROPERTY_APP_VERSION = "property.appVersion";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "property.expirationTime";
	private static final long EXPIRATION_TIME = 1000 * 3600 * 24 * 7;

	// PUSH SETTING PREFERENCES
	private static final String PREFERENCE_PUSHSETTINGS = "anb.ground.preference.pushSettings";
	private static final String PROPERTY_PUSH_PERMISSION = "anb.ground.property.push.permission";
	private static final String PROPERTY_PUSH_TOAST = "anb.ground.property.push.toast";
	private static final String PROPERTY_PUSH_VIBRATION = "anb.ground.property.push.vibration";
	
	// APP VALUES
	public static String regId;
	public static int appVer;
	public static String uuid;
	public static Typeface mFont;
	public static PushSettings pushSettings;
	
	// CLASS FIELDS
	private PackageInfo packageInfo;
	private String senderId = "886264887841";
	private GoogleCloudMessaging gcm;

	public final static GlobalApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		initGlobalApplication();
		initPushSettings();
	}

	// initialization

	public void initGlobalApplication() {
		instance = this;

		getAppVersion();
		getUuid();
		getFont();

		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
			Thread.setDefaultUncaughtExceptionHandler(new MainExceptionHandler(packageInfo));

		} catch (Throwable e) {
		}
	}

	private void getFont() {
		mFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaCdBlk.ttf");
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	private void getUuid() {
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uuid = tManager.getDeviceId();
	}

	private void getAppVersion() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			appVer = packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			throw new RuntimeException("could not found package name");
		}
	}
	
	// push settings initialization & termination

	private void initPushSettings() {
		final SharedPreferences prefs = getPushPreferences();
		pushSettings = new PushSettings();
		
		pushSettings.setPushAllowed(prefs.getBoolean(PROPERTY_PUSH_PERMISSION, true));
		pushSettings.setPushToastAllowed(prefs.getBoolean(PROPERTY_PUSH_TOAST, true));
		pushSettings.setPushVibrationAllowed(prefs.getBoolean(PROPERTY_PUSH_VIBRATION, true));
	}
	
	public void savePushSettings() {
		final SharedPreferences prefs = getPushPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putBoolean(PROPERTY_PUSH_PERMISSION, pushSettings.isPushAllowed());
		editor.putBoolean(PROPERTY_PUSH_TOAST, pushSettings.isPushToastAllowed());
		editor.putBoolean(PROPERTY_PUSH_VIBRATION, pushSettings.isPushVibrationAllowed());
		editor.commit();
	}

	private SharedPreferences getPushPreferences() {
		return getSharedPreferences(PREFERENCE_PUSHSETTINGS, Context.MODE_PRIVATE);
	}
	
	// GCM initialization & termination

	public void initGCM() {
		regId = getRegistrationId(this);

		if (regId.length() == 0)
			registerBackground();

		gcm = GoogleCloudMessaging.getInstance(this);
	}

	private void registerBackground() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if (gcm == null)
						gcm = GoogleCloudMessaging.getInstance(GlobalApplication.this);
					regId = gcm.register(senderId);

					setRegistrationId();

					Log.i("GCM", "newly registered with id : " + regId);
				} catch (IOException e) {
					regId = "";
				}

				return null;
			}

		}.execute(null, null);

	}

	private void unregisterBackground() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if (gcm != null)
						gcm = GoogleCloudMessaging.getInstance(GlobalApplication.this);
					
					if (gcm != null)
						gcm.unregister();

					removeRegistrationId();

					Log.i("GCM", "device unregistered");
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}

		}.execute(null, null);
	}

	protected void removeRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		SharedPreferences.Editor editor = prefs.edit();

		editor.remove(PROPERTY_REG_ID);
		editor.remove(PROPERTY_APP_VERSION);
		editor.remove(PROPERTY_ON_SERVER_EXPIRATION_TIME);
		editor.commit();
	}

	private void setRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		long expirationTime = System.currentTimeMillis() + EXPIRATION_TIME;

		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVer);
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId.length() == 0)
			return "";

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = appVer;

		if (registeredVersion != currentVersion || isRegistrationExpired())
			return "";

		return registrationId;
	}

	private boolean isRegistrationExpired() {
		final SharedPreferences prefs = getGCMPreferences();

		long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);

		return System.currentTimeMillis() > expirationTime;
	}

	private SharedPreferences getGCMPreferences() {
		return getSharedPreferences("Ground_GCM_Preferences", Context.MODE_PRIVATE);
	}
	
	// font changing method ; to be used in any activities

	public static final void setAppFont(ViewGroup mContainer) {
		if (mContainer == null || mFont == null)
			return;

		final int mCount = mContainer.getChildCount();

		// Loop through all of the children.
		for (int i = 0; i < mCount; ++i) {
			final View mChild = mContainer.getChildAt(i);
			if (mChild instanceof TextView) {
				// Set the font if it is a TextView.
				((TextView) mChild).setTypeface(mFont);
			} else if (mChild instanceof ViewGroup) {
				// Recursively attempt another ViewGroup.
				setAppFont((ViewGroup) mChild);
			}
		}
	}
	
	// termination

	@Override
	public void onTerminate() {
		super.onTerminate();
		instance = null;
	}

//	public boolean isLoggedIn() {
//		return LocalUser.getInstance().isLogin();
//	}

	public void logout() {
		unregisterBackground();
		finishFacebookSession();
		showLoginActivity();
	}

	public void finishFacebookSession() {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			session.closeAndClearTokenInformation();
			Log.i("FacebookLogin", "session closed");
		}
	}

	private void showLoginActivity() {
		Intent intent = new Intent(this, StartingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
