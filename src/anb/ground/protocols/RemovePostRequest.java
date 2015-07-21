package anb.ground.protocols;

public class RemovePostRequest implements DefaultRequest {
	private static final String protocol = "remove_post";

	private int teamId;
	private long postId;

	public RemovePostRequest(int teamId, long postId) {
		super();
		this.teamId = teamId;
		this.postId = postId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
