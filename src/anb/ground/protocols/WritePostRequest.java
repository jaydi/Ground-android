package anb.ground.protocols;

import anb.ground.models.Post;

public class WritePostRequest implements DefaultRequest {
	private final static String protocol = "write_post";

	private int teamId;
	private int type;
	private String message;
	private String extra;

	public WritePostRequest() {
		super();
	}

	public WritePostRequest(int teamId, int type, String message, String extra) {
		super();
		this.teamId = teamId;
		this.type = type;
		this.message = message;
		this.extra = extra;
	}

	public WritePostRequest(Post post) {
		super();
		this.teamId = post.getTeamId();
		this.type = post.getType();
		this.message = post.getMessage();
		this.extra = post.getExtra();
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
