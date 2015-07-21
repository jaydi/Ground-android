package anb.ground.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import anb.ground.R;
import anb.ground.app.GlobalApplication;
import anb.ground.utils.ProtocolCodec;
import anb.ground.utils.ToastUtils;
import android.os.Handler;

public class GroundIMClient {
	private static final String IM_ADDRESS = "altair.vps.phps.kr";
	private static final int IM_PORT = 8999;

	private static Socket socket;
	private static BufferedReader reader;
	private static PrintWriter writer;

	private static ExecutorService executor = Executors.newCachedThreadPool();

	public static void start(Handler handler) throws UnknownHostException, IOException {
		socket = new Socket(IM_ADDRESS, IM_PORT);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		writer.println(GlobalApplication.uuid);
		writer.flush();

		executor.execute(new GroundIMClientWorker(reader, handler));
	}

	public static void init(final Handler handler) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					start(handler);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (ConnectException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	public static void sendMessage(Object message) {
		if (writer != null) {
			writer.println(ProtocolCodec.encode(message));
			writer.flush();
		} else {
			ToastUtils.show(R.string.alert_server_disconnected);
		}
	}

	public static void close() {
		try {
			if (writer != null)
				writer.close();

			if (reader != null)
				reader.close();

			if (socket != null)
				socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
