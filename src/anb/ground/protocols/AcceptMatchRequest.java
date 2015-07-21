package anb.ground.protocols;

public class AcceptMatchRequest implements DefaultRequest {
	private static final String protocol = "accept_match";

	private long matchId;

	public AcceptMatchRequest(long matchId) {
		super();
		this.matchId = matchId;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
