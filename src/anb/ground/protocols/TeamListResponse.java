package anb.ground.protocols;

import java.util.List;

import anb.ground.models.TeamHint;

public class TeamListResponse extends DefaultResponse {
	private List<TeamHint> teamList;

	public List<TeamHint> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamHint> teamList) {
		this.teamList = teamList;
	}
}
