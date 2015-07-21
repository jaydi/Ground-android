package anb.ground.protocols;

public class TeamInfoRequest implements DefaultRequest {
	private static final String protocol = "team_info";

	private int teamId;

	public TeamInfoRequest(int teamId) {
		super();
		this.teamId = teamId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
