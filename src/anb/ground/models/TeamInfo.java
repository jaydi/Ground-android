package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamInfo implements Parcelable {
	public static final String EXTRA_TEAMINFO = "anb.ground.extra.teamInfo";
	public static final String EXTRA_TEAM_MEMBERS = "anb.ground.extra.teamMembers";
	
	private int id;
	private String name;
	private String imageUrl;
	private float avgBirth;
	private int score;
	private int win;
	private int draw;
	private int lose;
	private int membersCount;
	private String address;
	private double latitude;
	private double longitude;
	
	public TeamInfo() {
		super();
	}

	public TeamInfo(String name, String imageUrl, String address, double latitude, double longitude) {
		super();
		this.name = name;
		this.imageUrl = imageUrl;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public float getAvgBirth() {
		return avgBirth;
	}

	public void setAvgBirth(float avgBirth) {
		this.avgBirth = avgBirth;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public int getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(int membersCount) {
		this.membersCount = membersCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<TeamInfo> CREATOR = new Creator<TeamInfo>() {

		@Override
		public TeamInfo createFromParcel(Parcel source) {
			return new TeamInfo(source);
		}

		@Override
		public TeamInfo[] newArray(int size) {
			return new TeamInfo[size];
		}
		
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(imageUrl);
		dest.writeFloat(avgBirth);
		dest.writeInt(score);
		dest.writeInt(win);
		dest.writeInt(draw);
		dest.writeInt(lose);
		dest.writeInt(membersCount);
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}

	public TeamInfo(Parcel source) {
		id = source.readInt();
		name = source.readString();
		imageUrl = source.readString();
		avgBirth = source.readFloat();
		score = source.readInt();
		win = source.readInt();
		draw = source.readInt();
		lose = source.readInt();
		membersCount = source.readInt();
		address = source.readString();
		latitude = source.readDouble();
		longitude = source.readDouble();
	}

	public Team toTeam() {
		Team team = new Team();
		team.setId(id);
		team.setName(name);
		team.setImageUrl(imageUrl);
		team.setWin(win);
		team.setDraw(draw);
		team.setLose(lose);
		team.setAvgBirth(avgBirth);
		return team;
	}
}
