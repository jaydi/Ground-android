package anb.ground.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import anb.ground.app.GlobalApplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.util.Log;

public class BitmapWorkUtils {
	public static int scaleFactor = UIScaleUtils.getPixels(1);

	public static class IconParams {
		public static int width = 48 * scaleFactor;
		public static int height = 48 * scaleFactor;
	}

	public static class ThumnailParams {
		public static int width = 120 * scaleFactor;
		public static int height = 120 * scaleFactor;
	}

	public static class PreviewParams {
		public static int width = 240 * scaleFactor;
		public static int height = 180 * scaleFactor;
	}

	public static class ViewParams {
		public static int width = 480;
		public static int height = 360;
	}

	public static Bitmap decodeStreamScaledDown(InputStream stream, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		byte[] data = new byte[0];
		try {
			data = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	public static Bitmap decodeFileScaledDown(String path, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	public static String saveBitmapAndGetPath(String imagePath, Bitmap bitmap) {
		String tempPath = "tempImagePath";
		String newPath = "";

		try {
			FileOutputStream fos = GlobalApplication.getInstance().getApplicationContext().openFileOutput(tempPath, Context.MODE_PRIVATE);
			if (bitmap != null) {
				bitmap = fixOrientation(imagePath, bitmap);
				bitmap.compress(CompressFormat.JPEG, 25, fos);
			}
			fos.close();

			newPath = GlobalApplication.getInstance().getApplicationContext().getFileStreamPath(tempPath).getAbsolutePath();
			Log.i("bitmap scaler", "scaled down to : " + newPath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newPath;
	}

	private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
		}
		Log.i("bitmap util", "scale factor : " + inSampleSize);
		return inSampleSize;
	}

	public static Bitmap cropCircularView(Bitmap bitmap) {
		if (bitmap == null)
			return null;

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		float radius;

		if (bitmap.getWidth() > bitmap.getHeight())
			radius = bitmap.getHeight() / 2;
		else
			radius = bitmap.getWidth() / 2;

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap fixOrientation(String imagePath, Bitmap result) {
		int orientation = 1;

		try {
			ExifInterface exif = new ExifInterface(imagePath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			result = rotate(result, 90);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			result = rotate(result, 180);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			result = rotate(result, 270);
			break;
		}

		return result;
	}

	private static Bitmap rotate(Bitmap result, int rotation) {
		Matrix matrix = new Matrix();
		matrix.setRotate(rotation, (float) result.getWidth() / 2, (float) result.getHeight() / 2);
		
		return Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
	}
}
