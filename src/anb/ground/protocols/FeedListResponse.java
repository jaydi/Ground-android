package anb.ground.protocols;

import java.util.List;

import anb.ground.models.Feed;

public class FeedListResponse extends DefaultResponse {
	private List<Feed> feedList;

	public List<Feed> getFeedList() {
		return feedList;
	}

	public void setFeedList(List<Feed> feedList) {
		this.feedList = feedList;
	}
}
