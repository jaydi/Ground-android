package anb.ground.protocols;

public class StartSurveyRequest implements DefaultRequest {
	private static final String protocol = "start_survey";

	private long matchId;
	private int teamId;

	public StartSurveyRequest(long matchId, int teamId) {
		super();
		this.matchId = matchId;
		this.teamId = teamId;
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

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
