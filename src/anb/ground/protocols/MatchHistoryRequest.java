package anb.ground.protocols;

public class MatchHistoryRequest implements DefaultRequest {
	private final static String protocol = "match_history";

	private int teamId;
	private long cur;
	private boolean order;

	public MatchHistoryRequest(int teamId, long cur, boolean order) {
		super();
		this.teamId = teamId;
		this.cur = cur;
		this.order = order;
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

	public boolean getOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
