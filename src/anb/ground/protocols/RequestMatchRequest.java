package anb.ground.protocols;

public class RequestMatchRequest implements DefaultRequest {
	private static final String protocol = "request_match";

	private long matchId;
	private int homeTeamId;
	private int awayTeamId;

	public RequestMatchRequest(long matchId, int homeTeamId, int awayTeamId) {
		super();
		this.matchId = matchId;
		this.homeTeamId = homeTeamId;
		this.awayTeamId = awayTeamId;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
