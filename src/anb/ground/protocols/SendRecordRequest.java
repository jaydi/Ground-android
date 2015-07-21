package anb.ground.protocols;


public class SendRecordRequest implements DefaultRequest {

	private static final String protocol = "set_score";
	
	private long matchId;
	private int teamId;
	private int homeScore;
	private int awayScore;
	
	public SendRecordRequest(long matchId, int teamId, int homeScore, int awayScore) {
		super();
		this.matchId = matchId;
		this.teamId = teamId;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
