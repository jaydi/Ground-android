package anb.ground.protocols;

public class JoinMatchRequest implements DefaultRequest {
	private static final String protocol = "join_match";
	
	private long matchId;
	private int teamId;
	private boolean join;
	
	public JoinMatchRequest(long matchId, int teamId, boolean join) {
		super();
		this.matchId = matchId;
		this.teamId = teamId;
		this.join = join;
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

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
