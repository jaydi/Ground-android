package anb.ground.protocols;

import anb.ground.models.Match;

public class CreateMatchRequest implements DefaultRequest {
	private static final String protocol = "create_match";
	
	private int teamId;
	private long startTime;
	private long endTime;
	private int groundId;
	private int awayTeamId;
	private String description;
	private boolean open;
	
	public CreateMatchRequest(Match match) {
		teamId = match.getHomeTeamId();
		startTime = match.getStartTime();
		endTime = match.getEndTime();
		groundId = match.getGroundId();
		awayTeamId = match.getAwayTeamId();
		description = match.getDescription();
		open = match.isOpen();
	}
	
	public CreateMatchRequest() {
		super();
	}


	public int getTeamId() {
		return teamId;
	}


	public void setTeamId(int teamId) {
		this.teamId = teamId;
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


	public int getGroundId() {
		return groundId;
	}


	public void setGroundId(int groundId) {
		this.groundId = groundId;
	}


	public int getAwayTeamId() {
		return awayTeamId;
	}


	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public boolean isOpen() {
		return open;
	}


	public void setOpen(boolean open) {
		this.open = open;
	}


	@Override
	public String getProtocolName() {
		return protocol;
	}

}
