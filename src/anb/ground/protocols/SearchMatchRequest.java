package anb.ground.protocols;

import anb.ground.models.SMatch;

public class SearchMatchRequest implements DefaultRequest {
	private static final String protocol = "search_match";

	private long startTime;
	private long endTime;
	private double latitude;
	private double longitude;
	private double distance;

	public SearchMatchRequest(SMatch sMatch) {
		super();
		startTime = sMatch.getStartTime();
		endTime = sMatch.getEndTime();
		latitude = sMatch.getLatitude();
		longitude = sMatch.getLongitude();
		distance = sMatch.getDistance();
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
