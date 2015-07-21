package anb.ground.protocols;

public class NewPswdRequest implements DefaultRequest {
	private static final String protocolName = "pswdSearch";
	
	private String email;
	
	public NewPswdRequest(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getProtocolName() {
		return protocolName;
	}

}
