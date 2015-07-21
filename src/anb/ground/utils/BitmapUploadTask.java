package anb.ground.utils;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import anb.ground.R;
import anb.ground.server.GroundClient;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class BitmapUploadTask extends AsyncTask<String, Void, Bitmap> {
	private Handler handler;
	private String path;
	private int reqWidth = BitmapWorkUtils.ViewParams.width;
	private int reqHeight = BitmapWorkUtils.ViewParams.height;
	private boolean thumnail;

	public BitmapUploadTask(Handler handler, boolean thumnail) {
		this.handler = handler;
		this.thumnail = thumnail;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		path = params[0];

		if (path.startsWith("http"))
			return downloadBitmap(path);
		else if (path.startsWith("/"))
			return BitmapWorkUtils.decodeFileScaledDown(path, reqWidth, reqHeight);
		else
			return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			String newPath = BitmapWorkUtils.saveBitmapAndGetPath(path, result);
			GroundClient.uploadFile(handler, newPath, thumnail);
		} else {
			ToastUtils.show(R.string.alert_empty_image);
		}
	}

	private Bitmap downloadBitmap(String url) {
		// initialize the default HTTP client object
		final DefaultHttpClient client = new DefaultHttpClient();

		// forming a HttoGet request
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			// check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;

				try {
					// getting contents from the stream
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that android
					// understands
					final Bitmap bitmap = BitmapWorkUtils.decodeStreamScaledDown(inputStream, reqWidth, reqHeight);

					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// You Could provide a more explicit error message for IOException
			getRequest.abort();
			Log.e("ImageDownloader", "Something went wrong while" + " retrieving bitmap from " + url + e.toString());
		}

		return null;
	}

}
