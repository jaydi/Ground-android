package anb.ground.protocols;

import anb.ground.models.MatchInfo;

public class MatchInfoResponse extends DefaultResponse {
	private MatchInfo matchInfo;

	public MatchInfo getMatchInfo() {
		return matchInfo;
	}

	public void setMatchInfo(MatchInfo matchInfo) {
		this.matchInfo = matchInfo;
	}

}
