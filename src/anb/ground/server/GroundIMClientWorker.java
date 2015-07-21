package anb.ground.server;
import java.io.BufferedReader;
import java.io.IOException;

import android.os.Handler;
import android.os.Message;

public class GroundIMClientWorker implements Runnable {
	private BufferedReader reader;
	private Handler handler;
	
	public GroundIMClientWorker(BufferedReader reader, Handler handler) {
		this.reader = reader;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Message msg = new Message();
				msg.obj = line;
				handler.sendMessage(msg);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
