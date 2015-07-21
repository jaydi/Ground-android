package anb.ground.protocols;

public class WritePostResponse extends DefaultResponse {
	private long postId;

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}
	
	@Override
	public String toString() {
		return "post id : " + postId;
	}

}
