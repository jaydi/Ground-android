package anb.ground.models;

import anb.ground.utils.ProtocolCodec;
import anb.ground.utils.Validator;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	public static final int MEMBER_ID_NEW_MANAGER = -1;
	public static final int MEMBER_ID_INVITE_KAKAO = -2;
	public static final String EXTRA_USER_EMAIL = "anb.ground.extra.user.email";
	public static final String EXTRA_USER_PSWD = "anb.ground.extra.user.pswd";

	protected int id;
	protected String name;
	protected String email;
	protected String imageUrl;
	protected boolean isManager;

	public User() {
	}

	public static User newInstance(int id, String name, String imageUrl) {
		User user = new User();
		user.id = id;
		user.name = name;
		user.imageUrl = imageUrl;

		return user;
	}

	public static User newInstance(String data) {
		if (Validator.isEmpty(data))
			return null;
		User user = ProtocolCodec.decode(data, User.class);
		return user;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(imageUrl);
		dest.writeString(String.valueOf(isManager));
	}

	public User(Parcel source) {
		id = source.readInt();
		name = source.readString();
		email = source.readString();
		imageUrl = source.readString();
		isManager = Boolean.valueOf(source.readString());
	}

	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}

	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	@Override
	public boolean equals(Object o) {
		try {
			User user = (User) o;
			if (user.getId() == this.id)
				return true;
			else
				return false;
		} catch (ClassCastException e) {
			return false;
		}
	}
}
