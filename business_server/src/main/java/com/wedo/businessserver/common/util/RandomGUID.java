package com.wedo.businessserver.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RandomGUID {
	private static final Log logger = LogFactory.getLog(RandomGUID.class);
	private static Random myRand;
	private static SecureRandom mySecureRand = new SecureRandom();
	private static String sID;
	private static final int PAD_BELOW = 16;
	private static final int TWO_BYTES = 255;
	private static final int FILE_BUFFER_128 = 128;
	private static final int FILE_BUFFER_32 = 32;
	private static final int FILE_BUFFER_64 = 64;
	private static final int STRING_LENGTH_8 = 8;
	private static final int STRING_LENGTH_12 = 12;
	private static final int STRING_LENGTH_16 = 16;
	private static final int STRING_LENGTH_20 = 20;
	private String valueBeforeMD5 = "";

	private String valueAfterMD5 = "";

	static {
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			sID = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public RandomGUID() {
		getRandomGUID(false);
	}

	public RandomGUID(boolean secure) {
		getRandomGUID(secure);
	}

	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer(128);
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error: " + e);
		}

		try {
			long time = System.currentTimeMillis();
			long rand = 0L;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}
			sbValueBeforeMD5.append(sID);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));

			this.valueBeforeMD5 = sbValueBeforeMD5.toString();
			if (md5 != null) {
				md5.update(this.valueBeforeMD5.getBytes());
				byte[] array = md5.digest();
				StringBuffer sb = new StringBuffer(32);
				for (int j = 0; j < array.length; j++) {
					int b = array[j] & 0xFF;
					if (b < 16) {
						sb.append('0');
					}
					sb.append(Integer.toHexString(b));
				}
				this.valueAfterMD5 = sb.toString();
			}
		} catch (Exception e) {
			logger.error("Error:" + e);
		}
	}

	public String getValueBeforeMD5() {
		return this.valueBeforeMD5;
	}

	public void setValueBeforeMD5(String valueBeforeMD5) {
		this.valueBeforeMD5 = valueBeforeMD5;
	}

	public String getValueAfterMD5() {
		return this.valueAfterMD5;
	}

	public void setValueAfterMD5(String valueAfterMD5) {
		this.valueAfterMD5 = valueAfterMD5;
	}

	public String toString() {
		String raw = this.valueAfterMD5.toUpperCase(Locale.getDefault());
		StringBuffer sb = new StringBuffer(64);
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));
		return sb.toString();
	}
}
