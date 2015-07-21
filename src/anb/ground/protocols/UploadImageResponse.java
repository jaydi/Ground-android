package anb.ground.protocols;

public class UploadImageResponse extends DefaultResponse {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		return sb.append(",path=").append(path).toString();
	}
}
