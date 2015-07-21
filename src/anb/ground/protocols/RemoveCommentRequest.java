package anb.ground.protocols;

public class RemoveCommentRequest implements DefaultRequest {
	private static final String protocol = "remove_comment";

	private long postId;
	private long commentId;

	public RemoveCommentRequest(long postId, long commentId) {
		super();
		this.postId = postId;
		this.commentId = commentId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
