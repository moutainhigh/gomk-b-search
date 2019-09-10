package io.gomk.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
* @作者 杨永
* @创建日期 2014年2月20日
* @版本 V 1.0
*/
public class MD5{
 
	/**
	 * 纯加密
	 * @param plainText
	 * @return
	 */
	public static String MD5Purity(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
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
			plainText = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return plainText;
	}

	/**
	 * 重复
	 * @param plainText
	 * @param n
	 * @return
	 */
	public static String MD5Times(String plainText, int n) {
		for (int i = 0; i < n; i++) {
			plainText = MD5Purity(plainText);
		}
		return plainText.toUpperCase();
	}

	/**
	 * 加串
	 * @param plainText
	 * @param append
	 * @return
	 */
	public static String MD5Append(String plainText, String append) {
		return MD5Reverse(plainText + append).toUpperCase();
	}
	

	/**
	 * 逆序
	 * @param plainText
	 * @return
	 */
	public static String MD5Reverse(String plainText) {
		StringBuffer s = new StringBuffer(MD5Purity(plainText));
		s = s.reverse();
		plainText = s.toString();
		return MD5Purity(plainText).toUpperCase();
	}
	
	/**
	 * 签名生成算法
	 * @param appId
	 * @param appSecret
	 * @param state
	 * @return
	 */
	public static String MD5Signature(String appId,String appSecret,String state ){
		return MD5Purity(appId+MD5Purity(appSecret+state)).toLowerCase();
	}
	public static void main(String[] args) {
		System.out.println(MD5Append("111111", "sdaqw22"));
	}
}
