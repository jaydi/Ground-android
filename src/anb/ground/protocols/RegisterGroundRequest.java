package anb.ground.protocols;

import anb.ground.models.Ground;

public class RegisterGroundRequest implements DefaultRequest {
	private final static String protocol = "register_ground";
	
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	
	public RegisterGroundRequest(Ground ground) {
		name = ground.getName();
		address = ground.getAddress();
		latitude = ground.getLatitude();
		longitude = ground.getLongitude();
	}
	
	public RegisterGroundRequest(String name, String address, double latitude, double longitude) {
		super();
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public String getProtocolName() {
		return protocol;
	}

}
