package anb.ground.utils;

import java.util.Collections;
import java.util.List;

import anb.ground.models.Post;

public class AdManager {
	public static void addAdAsPost(List<Post> postList) {
		Collections.sort(postList, new PostComparator());
		
		int i = postList.size() / 10;
		for (int j = 1; j <= i; j++) {
			long id = postList.get(j * 10 - 1).getId() - 1;

			Post ad = new Post();
			ad.setType(Post.TYPE_AD);
			ad.setId(id);

			postList.add(ad);
		}

		Collections.sort(postList, new PostComparator());
	}
}
