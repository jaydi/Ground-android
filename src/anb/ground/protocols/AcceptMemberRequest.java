package anb.ground.protocols;

public class AcceptMemberRequest implements DefaultRequest {
	private static final String protocol = "accept_member";

	private int memberId;
	private int teamId;

	public AcceptMemberRequest(int memberId, int teamId) {
		super();
		this.memberId = memberId;
		this.teamId = teamId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
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
