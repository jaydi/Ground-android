package anb.ground.protocols;

public class RegisterRequest implements DefaultRequest {
	private static final String protocolName = "register";

	private String email;
	private String password;
	private String pushToken;
	private int os;
	private int appVer;
	private String deviceUuid;

	public RegisterRequest(String email, String password, String pushToken,
			int os, int appVer, String deviceUuid) {
		super();
		this.email = email;
		this.password = password;
		this.pushToken = pushToken;
		this.os = os;
		this.appVer = appVer;
		this.deviceUuid = deviceUuid;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public int getAppVer() {
		return appVer;
	}

	public void setAppVer(int appVer) {
		this.appVer = appVer;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getProtocolName() {
		return protocolName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("email=").append(email).append(",password=")
				.append(password).toString();
	}
}
