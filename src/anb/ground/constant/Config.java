package anb.ground.constant;

import android.util.Log;

public class Config {
	public static volatile boolean hasReceivedInfo = false;

	public static enum DeployPhase {
		Release,
		Develop,
		Home;
	}

	public static final DeployPhase DEPLOY_PHASE = DeployPhase.Develop;

	// db
	public static final String DB_DATABASE_NAME = "json_cache.db";
	public static final String DB_TABLE_NAME = "url_cache";

	public static final int MEMORY_LOGGER_LEVEL = Log.INFO;
	public static final int MEMORY_LOGGER_SIZE = 500;
	public static final int LOGGER_LEVEL = 0;
	public static final boolean isDebuggable = true;

	public static boolean isDevMode() {
		return DEPLOY_PHASE != DeployPhase.Release;
	}
}
