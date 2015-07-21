package anb.ground.protocols;

import anb.ground.models.TeamInfo;

public class TeamInfoResponse extends DefaultResponse {
	private TeamInfo teamInfo;

	public TeamInfo getTeamInfo() {
		return teamInfo;
	}

	public void setTeamInfo(TeamInfo teamInfo) {
		this.teamInfo = teamInfo;
	}

}
