package anb.ground.utils;

import anb.ground.app.GlobalApplication;

public class UIScaleUtils {
	public static int getPixels(int dp) {
		final float scale = GlobalApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
