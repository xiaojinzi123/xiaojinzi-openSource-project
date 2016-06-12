package com.huaishi.wwww.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 32位的加密类
 * 
 * @author cxj
 *
 */
public class Md5Util {

	/**
	 * 对字符串进行不可逆加密
	 * 
	 * @param sourceStr
	 *            要加密的字符串
	 * @return 返回null说明加密失败
	 */
	public static String MD5(String sourceStr) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

}
