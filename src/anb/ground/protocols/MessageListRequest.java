package anb.ground.protocols;

public class MessageListRequest implements DefaultRequest {
	private static final String protocol = "message_list";

	private long matchId;
	private int teamId;
	private long cur;

	public MessageListRequest(long matchId, int teamId, long cur) {
		super();
		this.matchId = matchId;
		this.teamId = teamId;
		this.cur = cur;
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

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
