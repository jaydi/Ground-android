package anb.ground.protocols;

public class JoinMemberListRequest implements DefaultRequest {
	private static final String protocol = "join_member_list";

	private long matchId;
	private int teamId;

	public JoinMemberListRequest(long matchId, int teamId) {
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
