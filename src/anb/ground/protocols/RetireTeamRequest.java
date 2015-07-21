package anb.ground.protocols;

public class RetireTeamRequest implements DefaultRequest {
	private static final String protocol = "retire_team";

	private int teamId;

	public RetireTeamRequest(int teamId) {
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
