package anb.ground.protocols;


public class FeedListRequest implements DefaultRequest {
	private static final String protocol = "feed_list";

	private long cur;

	public FeedListRequest(long cur) {
		super();
		this.cur = cur;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
