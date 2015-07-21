package anb.ground.protocols;

public class SearchTeamNearbyRequest implements DefaultRequest {
	private static final String protocol = "search_team_nearby";

	private double latitude;
	private double longitude;
	private int distance;

	public SearchTeamNearbyRequest(double latitude, double longitude, int distance) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
