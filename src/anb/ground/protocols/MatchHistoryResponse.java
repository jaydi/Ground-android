package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Match;

public class MatchHistoryResponse extends DefaultResponse {
	private List<Match> matchList;
	
	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("# of match history responsed : " + matchList.size());
		
		return sb.toString();
	}
}
