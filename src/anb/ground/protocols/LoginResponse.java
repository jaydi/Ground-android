package anb.ground.protocols;

public class LoginResponse extends DefaultResponse {
	private int userId;
	private String name;
	private String imageUrl;
	private String sessionKey;

	public String getSessionKey() {
		return sessionKey;
	}

	public void setUserKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		return sb.append(",userId=").append(userId).append(",name=").append(name).append(",imageUrl=").append(imageUrl).append(",sessionKey=")
				.append(sessionKey).toString();
	}
}
