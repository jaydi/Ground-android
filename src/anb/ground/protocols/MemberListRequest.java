package anb.ground.protocols;

public class MemberListRequest implements DefaultRequest {
	private static final String protocol = "member_list";

	private int teamId;

	public MemberListRequest(int teamId) {
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
