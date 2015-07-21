package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SMatch implements Parcelable {
	private int id;
	private long startTime;
	private long endTime;
	private String groundName;
	private int homeTeamId;
	private String homeTeamName;
	private double distance;

	private double latitude;
	private double longitude;

	public SMatch() {
		super();
	}

	public SMatch(int id) {
		super();
		this.id = id;
	}

	public SMatch(long startTime, long endTime, double latitude, double longitude, int distance) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getGroundName() {
		return groundName;
	}

	public void setGroundName(String groundName) {
		this.groundName = groundName;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public static Parcelable.Creator<SMatch> CREATOR = new Parcelable.Creator<SMatch>() {

		@Override
		public SMatch createFromParcel(Parcel source) {
			return new SMatch(source);
		}

		@Override
		public SMatch[] newArray(int size) {
			return new SMatch[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeLong(startTime);
		dest.writeLong(endTime);
		dest.writeString(groundName);
		dest.writeInt(homeTeamId);
		dest.writeString(homeTeamName);
		dest.writeDouble(distance);
	}

	public SMatch(Parcel source) {
		id = source.readInt();
		startTime = source.readLong();
		endTime = source.readLong();
		groundName = source.readString();
		homeTeamId = source.readInt();
		homeTeamName = source.readString();
		distance = source.readDouble();
	}

}
