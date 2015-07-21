package anb.ground.utils;

import anb.ground.app.GlobalApplication;

public class StringProvider {
	public static String getString(int resId) {
		return GlobalApplication.getInstance().getResources().getString(resId);
	}
}
