package anb.ground.protocols;


public class SearchTeamRequest implements DefaultRequest {
	private static final String protocol = "search_team";

	private String name;

	public SearchTeamRequest(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
