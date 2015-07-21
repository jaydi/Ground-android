package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Comment;

public class CommentListResponse extends DefaultResponse {
	private List<Comment> commentList;

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
}
