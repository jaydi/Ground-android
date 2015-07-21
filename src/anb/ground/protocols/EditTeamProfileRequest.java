package anb.ground.protocols;

import anb.ground.models.TeamInfo;

public class EditTeamProfileRequest implements DefaultRequest {
	private static final String protocol = "edit_team_profile";
	
	private int teamId;
	private String address;
	private double latitude;
	private double longitude;
	private String imageUrl;
	
	public EditTeamProfileRequest(TeamInfo teamInfo) {
		teamId = teamInfo.getId();
		address = teamInfo.getAddress();
		latitude = teamInfo.getLatitude();
		longitude = teamInfo.getLongitude();
		imageUrl = teamInfo.getImageUrl();
	}
	
	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
