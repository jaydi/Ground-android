package anb.ground.utils;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapFileLoadTask extends AsyncTask<String, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private String imagePath;

	public BitmapFileLoadTask(ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		imagePath = params[0];
		return BitmapWorkUtils.decodeFileScaledDown(imagePath, BitmapWorkUtils.ThumnailParams.width, BitmapWorkUtils.ThumnailParams.height);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (imageViewReference != null && result != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null)
				imageView.setImageBitmap(BitmapWorkUtils.fixOrientation(imagePath, result));
		}
	}

}
