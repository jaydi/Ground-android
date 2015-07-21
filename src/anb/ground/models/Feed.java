package anb.ground.models;

import java.util.Date;

import anb.ground.utils.ProtocolCodec;

public class Feed {
	public static final int ICON_ALARM = 0;
	public static final int ICON_NOTICE = 1;
	public static final int ICON_CHECKED = 2;

	public static final int TYPE_GUIDE = -1;

	public static final int TYPE_JOIN_TEAM = 0;
	public static final int TYPE_ACCEPT_MEMBER = 1;
	public static final int TYPE_DENY_MEMBER = 2;
	public static final int TYPE_DENY_TEAM = 3;
	public static final int TYPE_LEAVE_TEAM = 4;

	public static final int TYPE_REQUEST_MATCH = 5;
	public static final int TYPE_INVITE_TEAM = 6;
	public static final int TYPE_MATCHING_COMPLETED = 7;
	public static final int TYPE_DENY_MATCH = 8;

	public static final int TYPE_DO_SURVEY = 9;
	public static final int TYPE_SET_SCORE = 10;
	public static final int TYPE_ACCEPT_SCORE = 11;
	public static final int TYPE_SCORE_COMPLETED = 12;

	private long id;
	private int type;
	private FeedMessage message;
	private long createdAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public FeedMessage getMessage() {
		return message;
	}

	public void setMessageObject(FeedMessage message) {
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = ProtocolCodec.decode(message, FeedMessage.class);
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public static Feed guideFeed() {
		Feed guide = new Feed();
		guide.setId(1);
		guide.setType(TYPE_GUIDE);
		guide.setMessageObject(FeedMessage.guideMessage());
		guide.setCreatedAt(new Date().getTime());
		return guide;
	}
}
