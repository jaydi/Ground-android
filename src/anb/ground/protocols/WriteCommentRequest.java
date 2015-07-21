package anb.ground.protocols;

public class WriteCommentRequest implements DefaultRequest {
	private static final String protocol = "write_comment";

	private int teamId;
	private long postId;
	private String message;

	public WriteCommentRequest(int teamId, long postId, String message) {
		super();
		this.teamId = teamId;
		this.postId = postId;
		this.message = message;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
