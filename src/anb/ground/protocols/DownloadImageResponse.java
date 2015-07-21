package anb.ground.protocols;

import android.graphics.Bitmap;

public class DownloadImageResponse extends DefaultResponse {
	private Bitmap image;

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
