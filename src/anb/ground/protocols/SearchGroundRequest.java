package anb.ground.protocols;

public class SearchGroundRequest implements DefaultRequest {
	private static final String protocol = "search_ground";

	private String name;

	public SearchGroundRequest() {
		super();
	}

	public SearchGroundRequest(String name) {
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
