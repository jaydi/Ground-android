package anb.ground.protocols;

public class MatchListRequest implements DefaultRequest {
	private final static String protocol = "match_list";

	private int teamId;

	public MatchListRequest(int teamId) {
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
