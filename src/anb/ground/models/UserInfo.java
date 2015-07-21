package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
	public static final String EXTRA_USER_INFO = "anb.ground.exra.userInfo";

	private int id;
	private String name;
	private int birthYear;
	private int position = -1;
	private int height;
	private int weight;
	private int mainFoot = -1;
	private String location1;
	private String location2;
	private int occupation = -1;
	private String phoneNumber;
	private String profileImageUrl;

	public UserInfo() {
		super();
	}

	public UserInfo(String name, int birthYear, int position, int height, int weight, int mainFoot, String location1, String location2, int occupation,
			String phoneNumber, String profileImageUrl) {
		super();
		this.name = name;
		this.birthYear = birthYear;
		this.position = position;
		this.height = height;
		this.weight = weight;
		this.mainFoot = mainFoot;
		this.location1 = location1;
		this.location2 = location2;
		this.occupation = occupation;
		this.phoneNumber = phoneNumber;
		this.profileImageUrl = profileImageUrl;
	}

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

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getMainFoot() {
		return mainFoot;
	}

	public void setMainFoot(int mainFoot) {
		this.mainFoot = mainFoot;
	}

	public String getLocation1() {
		return location1;
	}

	public void setLocation1(String location1) {
		this.location1 = location1;
	}

	public String getLocation2() {
		return location2;
	}

	public void setLocation2(String location2) {
		this.location2 = location2;
	}

	public int getOccupation() {
		return occupation;
	}

	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// this is used to regenerate object
	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
		public UserInfo createFromParcel(Parcel in) {
			return new UserInfo(in);
		}

		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(birthYear);
		dest.writeInt(position);
		dest.writeInt(height);
		dest.writeInt(weight);
		dest.writeInt(mainFoot);
		dest.writeString(location1);
		dest.writeString(location2);
		dest.writeInt(occupation);
		dest.writeString(phoneNumber);
		dest.writeString(profileImageUrl);
	}

	// constructor that takes a Parcel and gives an object populated with it's values
	private UserInfo(Parcel in) {
		id = in.readInt();
		name = in.readString();
		birthYear = in.readInt();
		position = in.readInt();
		height = in.readInt();
		weight = in.readInt();
		mainFoot = in.readInt();
		location1 = in.readString();
		location2 = in.readString();
		occupation = in.readInt();
		phoneNumber = in.readString();
		profileImageUrl = in.readString();
	}
}
