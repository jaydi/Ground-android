package anb.ground.protocols;

public class JoinTeamRequest implements DefaultRequest {
	private static final String protocol = "join_team";

	private int teamId;

	public JoinTeamRequest(int teamId) {
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
