package anb.ground.protocols;

public class RemoveFeedRequest implements DefaultRequest {
	private static final String protocol = "remove_feed";

	private long feedId;

	public RemoveFeedRequest(long feedId) {
		this.feedId = feedId;
	}

	public long getFeedId() {
		return feedId;
	}

	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
