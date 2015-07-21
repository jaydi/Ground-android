package anb.ground.protocols;

public class PushSurveyRequest implements DefaultRequest {
	private static final String protocol = "push_survey";

	private long matchId;
	private int teamId;

	public PushSurveyRequest(long matchId, int teamId) {
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
