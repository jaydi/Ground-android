package anb.ground.utils;

import anb.ground.R;
import anb.ground.protocols.DownloadImageResponse;
import anb.ground.server.GroundClient;
import anb.ground.server.ResponseHandler;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

public class ImageUtils {
	public static void getThumnailImageFile(ImageView imageView, String imagePath) {
		if (imagePath.startsWith("http"))
			new BitmapUrlLoadTask(imageView, false).execute(imagePath);
		else if (imagePath.startsWith("/"))
			new BitmapFileLoadTask(imageView).execute(imagePath);
	}

	public static void uploadImage(Handler handler, String imagePath) {
		if (!Validator.isEmpty(imagePath))
			new BitmapUploadTask(handler, true).execute(imagePath);
		else
			ToastUtils.show(R.string.alert_empty_image);
	}

	public static void getIconImage(ImageView imageView, String imagePath) {
		Bitmap bitmap = BitmapCache.getBitmapItem(imagePath);

		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
			download(imageView, imagePath, BitmapWorkUtils.IconParams.width, BitmapWorkUtils.IconParams.height, false, true);
	}

	public static void getThumnailImage(ImageView imageView, String imagePath) {
		Bitmap bitmap = BitmapCache.getBitmapItem(imagePath);

		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else if (!Validator.isEmpty(imagePath))
			if (imagePath.startsWith("/") || imagePath.startsWith("http"))
				getThumnailImageFile(imageView, imagePath);
			else
				download(imageView, imagePath, BitmapWorkUtils.ThumnailParams.width, BitmapWorkUtils.ThumnailParams.height, false, true);
	}

	public static void getThumnailImageCircular(ImageView imageView, String imagePath) {
		Bitmap bitmap = BitmapCache.getBitmapItem(imagePath);

		if (bitmap != null)
			imageView.setImageBitmap(BitmapWorkUtils.cropCircularView(bitmap));
		else
			download(imageView, imagePath, BitmapWorkUtils.ThumnailParams.width, BitmapWorkUtils.ThumnailParams.height, true, true);
	}

	public static void getPreviewImage(ImageView imageView, String imagePath) {
		Bitmap bitmap = BitmapCache.getBitmapItem(imagePath);

		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
			download(imageView, imagePath, BitmapWorkUtils.PreviewParams.width, BitmapWorkUtils.PreviewParams.height, false, false);
	}

	public static void getViewImage(ImageView imageView, String imagePath) {
		download(imageView, imagePath, BitmapWorkUtils.ViewParams.width, BitmapWorkUtils.ViewParams.height, false, false);
	}

	private static void download(final ImageView imageView, String imagePath, int scaledWidth, int scaledHeight, final boolean cropCircle, boolean thumnail) {
		if (!Validator.isEmpty(imagePath))
			if (imagePath.startsWith("http"))
				new BitmapUrlLoadTask(imageView, cropCircle).execute(imagePath);
			else
				GroundClient.downloadBinary(new ResponseHandler<DownloadImageResponse>() {

					@Override
					public void onReceiveOK(DownloadImageResponse response) {
						Bitmap bitmap = response.getImage();
						if (cropCircle)
							bitmap = BitmapWorkUtils.cropCircularView(bitmap);
						imageView.setImageBitmap(bitmap);
					}

				}, imagePath, scaledWidth, scaledHeight, thumnail);
	}
}
