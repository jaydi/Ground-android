package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Match;

public class MatchListResponse extends DefaultResponse {
	private List<Match> matchList;

	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}
}
