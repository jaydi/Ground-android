package anb.ground.protocols;

public class UserInfoRequest implements DefaultRequest {
	private static final String protocol = "user_info";

	private int userId;

	public UserInfoRequest(int userId) {
		super();
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
