package anb.ground.protocols;

import java.util.List;

import anb.ground.models.SMatch;

public class SearchMatchResponse extends DefaultResponse {
	private List<SMatch> matchList;

	public List<SMatch> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<SMatch> matchList) {
		this.matchList = matchList;
	}

}
