package anb.ground.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.pm.PackageInfo;

public class MainExceptionHandler implements UncaughtExceptionHandler {
	private UncaughtExceptionHandler defaultHandler;

	@SuppressWarnings("unused")
	private PackageInfo info;

	public MainExceptionHandler(PackageInfo info) {
		this.info = info;
		this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		defaultHandler.uncaughtException(thread, ex);
	}

	public static String toStrackTrace(Throwable ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String stackTrace = sw.toString();
		pw.close();
		return stackTrace;
	}
}
