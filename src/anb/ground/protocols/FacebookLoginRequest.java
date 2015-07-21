package anb.ground.protocols;


public class FacebookLoginRequest implements DefaultRequest {
	private static final String protocol = "facebook_login";
	
	private String email;
	private String name;
	private String imageUrl;
	private String pushToken;
	private int os;
	private int appVer;
	private String deviceUuid;

	public FacebookLoginRequest(String email, String name, String imageUrl, String pushToken, int os, int appVer, String deviceUuid) {
		super();
		this.email = email;
		this.name = name;
		this.imageUrl = imageUrl;
		this.pushToken = pushToken;
		this.os = os;
		this.appVer = appVer;
		this.deviceUuid = deviceUuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
