package anb.ground.utils;

import java.util.Comparator;

import anb.ground.models.Comment;

public class CommentComparator implements Comparator<Comment> {

	@Override
	public int compare(Comment lhs, Comment rhs) {
		return (lhs.getId() > rhs.getId())? 0 : -1;
	}

}
