package anb.ground.models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import anb.ground.app.GlobalApplication;
import anb.ground.utils.ProtocolCodec;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocalUser {
	public static final String PREFERENCE_LOCALUSER = "anb.ground.preference.localUser";
	public static final String PROPERTY_USER = "anb.ground.property.user";
	public static final String PROPERTY_SESSIONKEY = "anb.ground.property.sessionKey";

	private User user;
	private String sessionKey;
	private List<Integer> managingTeamIdList = new ArrayList<Integer>();

	private static volatile LocalUser instance;
	private SharedPreferences sharedPreference;

	// setting local user

	private LocalUser() {
		sharedPreference = GlobalApplication.getInstance().getSharedPreferences(PREFERENCE_LOCALUSER, Context.MODE_PRIVATE);
		user = User.newInstance(sharedPreference.getString(PROPERTY_USER, null));
		sessionKey = sharedPreference.getString(PROPERTY_SESSIONKEY, null);
	}

	public static LocalUser getInstance() {
		if (instance == null) {
			synchronized (LocalUser.class) {
				if (instance != null)
					return instance;
				instance = new LocalUser();
			}
		}
		return instance;
	}

	public boolean isLoggedin() {
		return user != null && sessionKey != null;
	}

	public void logout() {
		removeSessionKey();
		removeUser();
		GlobalApplication.getInstance().logout();
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		sharedPreference.edit().putString(PROPERTY_SESSIONKEY, sessionKey).commit();
	}

	public String getSessionKey() {
		if (sessionKey == null) {
			sessionKey = sharedPreference.getString(PROPERTY_SESSIONKEY, null);
		}
		return sessionKey;
	}

	public void removeSessionKey() {
		sessionKey = null;
		sharedPreference.edit().remove(PROPERTY_SESSIONKEY).commit();
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null)
			sharedPreference.edit().putString(PROPERTY_USER, ProtocolCodec.encode(user)).commit();
		else
			sharedPreference.edit().putString(PROPERTY_USER, null).commit();
	}

	public User getUser() {
		if (user == null) {
			user = User.newInstance(sharedPreference.getString(PROPERTY_USER, null));
		}
		return user;
	}

	public void removeUser() {
		user = null;
		sharedPreference.edit().remove(PROPERTY_USER).commit();
	}

	public static void log() {
		Log.i("new local user", getInstance().toString());
	}

	// user location

	public static Location lastLocation() {
		LocationManager manager = (LocationManager) GlobalApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
		Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location == null) {
			location = new Location("");
			location.setLatitude(37.4606018);
			location.setLongitude(126.9534072);
		}

		return location;
	}

	// team managing information

	public void setManagingTeamIdList(List<Integer> managingTeamIdList) {
		this.managingTeamIdList = managingTeamIdList;
		if (managingTeamIdList != null) {
			saveManagingTeamIdList(managingTeamIdList);
		}
	}

	private void saveManagingTeamIdList(List<Integer> managingTeamIdList) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(GlobalApplication.getInstance().openFileOutput(user.getId() + "managingTeamIdList", Context.MODE_PRIVATE));
			for (int managingId : managingTeamIdList) {
				dos.writeInt(managingId);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null)
					dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean managing(int teamId) {
		for (int managingId : getManagingTeamIdList())
			if (managingId == teamId)
				return true;

		return false;
	}

	private List<Integer> getManagingTeamIdList() {
		if (managingTeamIdList == null)
			managingTeamIdList = loadManagingTeamIdList();
		return managingTeamIdList;
	}

	private List<Integer> loadManagingTeamIdList() {
		DataInputStream dis = null;
		List<Integer> managingTeamIdList = new ArrayList<Integer>();
		try {
			dis = new DataInputStream(GlobalApplication.getInstance().openFileInput(user.getId() + "managingTeamIdList"));
			while (true) {
				Integer id = dis.readInt();
				if (id != null)
					managingTeamIdList.add(id);
				else
					break;
			}
		} catch (FileNotFoundException e) {
			return managingTeamIdList;
		} catch (IOException e) {
			return managingTeamIdList;
		}
		
		return managingTeamIdList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id : " + user.id + " name : " + user.name + " sessionKey : " + sessionKey);

		return sb.toString();
	}
}
