package anb.ground.server;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import anb.ground.constant.Config;
import anb.ground.models.LocalUser;
import anb.ground.protocols.DefaultResponse;
import anb.ground.utils.ProtocolCodec;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpConnectionMultipart<T extends DefaultResponse> implements Runnable {
	private static final int TIMEOUT_CONNECTION = 5000;
	private static final int TIMEOUT_SOCKET = 5000;
	private static final String url = HttpConnectionMultipart.initServerUrl();
	private static final String protocolName = "upload";

	private static String initServerUrl() {
		String url = "";
		switch (Config.DEPLOY_PHASE) {
		case Release:
			url = "http://altair.vps.phps.kr:9000/";
			break;
		case Develop:
			url = "http://altair.vps.phps.kr:9000/";
			break;
		case Home:
			url = "http://altair.vps.phps.kr:9000/";
			break;
		}
		return url;
	}

	private static HttpClient httpClient;
	private static ExecutorService executerService = Executors.newCachedThreadPool();

	private Handler handler;
	private boolean thumnail;
	private String imagePath;
	private Class<T> responseClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void execute(Handler handler, String imagePath, boolean thumnail, Class<?> responseClass) {
		if (httpClient == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
			httpClient = new DefaultHttpClient(httpParameters);

			ClientConnectionManager mgr = httpClient.getConnectionManager();
			HttpParams params = httpClient.getParams();
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
		}
		executerService.execute(new HttpConnectionMultipart(handler, imagePath, thumnail, responseClass));
	}

	public HttpConnectionMultipart(Handler handler, String imagePath, boolean thumnail, Class<T> responseClass) {
		this.handler = handler;
		this.imagePath = imagePath;
		this.thumnail = thumnail;
		this.responseClass = responseClass;
	}

	@Override
	public void run() {
		T response = runRequest();
		if (handler == null)
			return;
		Message msg = new Message();
		msg.obj = response;
		handler.sendMessage(msg);
	}

	private T runRequest() {
		T response;
		try {
			response = responseClass.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		HttpResponse httpResponse = null;
		try {
			httpResponse = handlePostRequest();
		} catch (Exception e) {
			response.setHttpStatus(-1);
			response.setMsg(e.toString());
			e.printStackTrace();
			return response;
		}

		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		if (httpStatus / 100 != 2) {
			response.setMsg("서버로부터 데이터를 가져오지 못했습니다 (code:" + httpStatus + ", data: " + httpResponse.getStatusLine().toString()
					+ ")");
			response.setHttpStatus(httpStatus);
			return response;
		}

		HttpEntity entity = httpResponse.getEntity();
		String entityString = null;
		try {
			entityString = EntityUtils.toString(entity);
			entity.consumeContent();
			entityString = entityString.trim();
		} catch (Exception e) {
		}

		Log.i(getClass().getSimpleName(), String.format("response => %s", entityString));
		response = ProtocolCodec.decode(entityString, responseClass);
		response.setHttpStatus(httpStatus);
		return response;
	}

	private HttpResponse handlePostRequest() throws Exception {
		HttpPost httpPost = new HttpPost(url + protocolName);
		httpPost.addHeader("sessionKey", LocalUser.getInstance().getSessionKey());

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart("file", new FileBody(new File(imagePath), "application/octet-stream"));
		entity.addPart("thumbnail", new StringBody(String.valueOf(thumnail)));

		httpPost.setEntity(entity);
		
		return httpClient.execute(httpPost);
	}
}
