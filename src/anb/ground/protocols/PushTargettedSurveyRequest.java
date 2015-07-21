package anb.ground.protocols;

import java.util.List;

public class PushTargettedSurveyRequest implements DefaultRequest {
	private static final String protocol = "push_targetted_survey";

	private long matchId;
	private int teamId;
	private List<Integer> pushIds;

	public PushTargettedSurveyRequest(long matchId, int teamId, List<Integer> pushIds) {
		super();
		this.matchId = matchId;
		this.teamId = teamId;
		this.pushIds = pushIds;
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

	public List<Integer> getPushIds() {
		return pushIds;
	}

	public void setPushIds(List<Integer> pushIds) {
		this.pushIds = pushIds;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
