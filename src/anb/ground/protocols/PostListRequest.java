package anb.ground.protocols;

public class PostListRequest implements DefaultRequest {
	private final static String protocol = "post_list";
	private int teamId;
	private long cur = 0;

	public PostListRequest(int teamId, long cur) {
		super();
		this.teamId = teamId;
		this.cur = cur;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
