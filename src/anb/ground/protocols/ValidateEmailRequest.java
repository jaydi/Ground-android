package anb.ground.protocols;

public class ValidateEmailRequest implements DefaultRequest {
	private static final String protocol = "validate_email";

	private String email;

	public ValidateEmailRequest() {
		super();
	}

	public ValidateEmailRequest(String email) {
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
		return protocol;
	}

}
