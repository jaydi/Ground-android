package anb.ground.protocols;

public class RequestMatchResponse extends DefaultResponse {
	private long matchId;
	private int matchStatus;

	public int getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(int matchStatus) {
		this.matchStatus = matchStatus;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

}
