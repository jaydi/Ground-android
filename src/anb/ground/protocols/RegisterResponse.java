package anb.ground.protocols;

public class RegisterResponse extends DefaultResponse {
	private int userId;
	private String email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		return sb.append(",userId=").append(userId).append(",email=").append(email).append(",sessionKey=").append(sessionKey)
				.toString();
	}
}