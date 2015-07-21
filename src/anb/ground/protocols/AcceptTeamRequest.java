package anb.ground.protocols;

public class AcceptTeamRequest implements DefaultRequest {
	private static final String protocol = "accept_team";

	private int teamId;

	public AcceptTeamRequest(int teamId) {
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
