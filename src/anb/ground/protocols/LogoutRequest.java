package anb.ground.protocols;

public class LogoutRequest implements DefaultRequest {
	private static final String protocol = "logout";

	private String deviceUuid;

	public LogoutRequest(String deviceUuid) {
		super();
		this.deviceUuid = deviceUuid;
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
