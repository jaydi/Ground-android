package anb.ground.protocols;

import anb.ground.models.TeamInfo;


public class RegisterTeamRequest implements DefaultRequest {
	private final static String protocol = "register_team";

	private String name;
	private String teamImageUrl;
	private String address;
	private double latitude;
	private double longitude;

	public RegisterTeamRequest() {
		super();
	}
	
	public RegisterTeamRequest(TeamInfo team) {
		name = team.getName();
		teamImageUrl = team.getImageUrl();
		address = team.getAddress();
		latitude = team.getLatitude();
		longitude = team.getLongitude();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeamImageUrl() {
		return teamImageUrl;
	}

	public void setTeamImageUrl(String teamImageUrl) {
		this.teamImageUrl = teamImageUrl;
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
