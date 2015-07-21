package anb.ground.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ground implements Parcelable {
	private int id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;

	public Ground() {
		super();
	}

	public Ground(int id, String name, String address, double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Ground(String name, String address, double latitude, double longitude) {
		super();
		this.name = name;
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
	public boolean equals(Object o) {
		if(o == this)
			return true;
		else if(o == null || o.getClass() != this.getClass())
			return false;
		
		Ground g = (Ground) o;
		return g.getId() == this.getId();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Ground> CREATOR = new Parcelable.Creator<Ground>() {

		@Override
		public Ground createFromParcel(Parcel source) {
			return new Ground(source);
		}

		@Override
		public Ground[] newArray(int size) {
			return new Ground[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
	}
	
	public Ground(Parcel source) {
		id = source.readInt();
		name = source.readString();
		address = source.readString();
		latitude = source.readDouble();
		longitude = source.readDouble();
	}
}
