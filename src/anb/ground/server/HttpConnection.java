package anb.ground.server;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import anb.ground.constant.Config;
import anb.ground.models.LocalUser;
import anb.ground.protocols.DefaultRequest;
import anb.ground.protocols.DefaultResponse;
import anb.ground.protocols.DefaultResponse.StatusCode;
import anb.ground.utils.ProtocolCodec;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpConnection<T extends DefaultResponse> implements Runnable {
	private static final int TIMEOUT_CONNECTION = 5000;
	private static final int TIMEOUT_SOCKET = 5000;
	private static final String url = HttpConnection.initServerUrl();

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
	private DefaultRequest request;
	private Class<T> responseClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void execute(Handler handler, DefaultRequest request, Class<?> responseClass) {
		if (httpClient == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
			httpClient = new DefaultHttpClient(httpParameters);

			ClientConnectionManager mgr = httpClient.getConnectionManager();
			HttpParams params = httpClient.getParams();
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
		}
		executerService.execute(new HttpConnection(handler, request, responseClass));
	}

	public HttpConnection(Handler handler, DefaultRequest request, Class<T> responseClass) {
		this.handler = handler;
		this.request = request;
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
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		HttpResponse httpResponse = null;
		try {
			httpResponse = handlePostRequest();
		} catch (UnknownHostException e) {
			response.setCode(StatusCode.NETWORK_ERROR.code());
			response.setMsg(e.toString());
			return response;
		} catch (SocketTimeoutException e) {
			response.setCode(StatusCode.NETWORK_ERROR.code());
			response.setMsg(e.toString());
			return response;
		} catch (ConnectTimeoutException e) {
			response.setCode(StatusCode.NETWORK_ERROR.code());
			response.setMsg(e.toString());
			return response;
		} catch (Exception e) {
			response.setCode(StatusCode.UNDEFINED.code());
			response.setMsg(e.toString());
			return response;
		}

		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		if (httpStatus / 100 != 2) {
			response.setMsg("서버로부터 데이터를 가져오지 못했습니다 (code:" + httpStatus + ", data: " + httpResponse.getStatusLine().toString() + ")");
			response.setHttpStatus(httpStatus);
			response.setCode(StatusCode.ERROR.code());
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
		HttpPost httpPost = new HttpPost(url + request.getProtocolName());
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("SessionKey", LocalUser.getInstance().getSessionKey());
		httpPost.addHeader("Content-type", "application/json");
		try {
			String body = ProtocolCodec.encode(request);
			Log.i("request:" + request.getProtocolName(), body);
			httpPost.setEntity(new StringEntity(body, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return httpClient.execute(httpPost);
	}
}
