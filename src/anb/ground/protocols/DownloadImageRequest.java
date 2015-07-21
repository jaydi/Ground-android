package anb.ground.protocols;

public class DownloadImageRequest implements DefaultRequest {
	private final static String protocol = "download";
	private String path;
	private boolean thumbnail;

	public DownloadImageRequest(String path, boolean thumbnail) {
		super();
		this.path = path;
		this.thumbnail = thumbnail;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(boolean thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public String getProtocolName() {
		return protocol;
	}

}
