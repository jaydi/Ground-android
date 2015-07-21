package anb.ground.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
	public static String encrypt(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(str.getBytes());
			byte data[] = digest.digest();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++)
				sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
