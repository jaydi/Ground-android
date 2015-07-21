package anb.ground.protocols;

import java.util.List;

import anb.ground.models.TeamInfo;

public class SearchTeamNearbyResponse extends DefaultResponse {
	private List<TeamInfo> teamList;

	public List<TeamInfo> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamInfo> teamList) {
		this.teamList = teamList;
	}

}
