package anb.ground.utils;

import java.util.Comparator;

import anb.ground.models.Post;

public class PostComparator implements Comparator<Post> {

	@Override
	public int compare(Post lhs, Post rhs) {
		return (lhs.getId() < rhs.getId())? 0 : -1;
	}

}
