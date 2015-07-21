package anb.ground.protocols;


public class DefaultResponse {
	public enum StatusCode {
		UNDEFINED(-1),
		OK(0),
		INVALID_PARAMETERS(100),
		INVALID_SESSION_KEY(101),
		EMAIL_NOT_EXIST(102),
		DUPLICATED_EMAIL(200),
		WRONG_PASSWORD(201),
		WRONG_EMAIL(202),
		ERROR(500),
		NETWORK_ERROR(-2);

		private int code;

		private StatusCode(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}

		public static StatusCode newInstance(int code) {
			for (StatusCode responseCode : values()) {
				if (responseCode.code == code)
					return responseCode;
			}
			return UNDEFINED;
		}
	}

	protected int code;
	protected String msg;
	private int httpStatus;
	private StatusCode statusCode = null;

	public DefaultResponse() {
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public StatusCode getStatusCode() {
		if (statusCode == null)
			statusCode = StatusCode.newInstance(code);
		return statusCode;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isOK() {
		return httpStatus / 200 != 2 && getStatusCode().equals(StatusCode.OK);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("code=").append(code).append(",msg=").append(msg).toString();
	}
}
