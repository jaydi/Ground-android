package anb.ground.protocols;


public class TeamListRequest implements DefaultRequest {
	private static final String protocol = "team_list";

	public TeamListRequest() {
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}
}
