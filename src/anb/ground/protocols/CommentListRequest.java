package anb.ground.protocols;

public class CommentListRequest implements DefaultRequest {
	private static final String protocol = "comment_list";

	private int teamId;
	private long postId;

	public CommentListRequest(int teamId, long postId) {
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
