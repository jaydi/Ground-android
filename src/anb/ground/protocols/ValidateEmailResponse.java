package anb.ground.protocols;

public class ValidateEmailResponse extends DefaultResponse {
	private boolean validEmail;

	public boolean isValidEmail() {
		return validEmail;
	}

	public void setValidEmail(boolean validEmail) {
		this.validEmail = validEmail;
	}
}
