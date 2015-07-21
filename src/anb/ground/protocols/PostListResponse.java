package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Post;

public class PostListResponse extends DefaultResponse {
	private List<Post> postList;

	public PostListResponse() {
		super();
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPosts(List<Post> posts) {
		this.postList = posts;
	}
	
}
