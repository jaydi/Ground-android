package anb.ground.protocols;

public class WannabeListRequest implements DefaultRequest {
	private static final String protocol = "pending_list";

	private int teamId;

	public WannabeListRequest(int teamId) {
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
