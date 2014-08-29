package com.neekle.kunlunandroid.security;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtils {

	private final static String DES = "DES";

	public static byte[] encryptDES(String encryptString, String encryptKey)
			throws Exception {
		byte[] iv = encryptKey.getBytes();

		IvParameterSpec zeroIv = new IvParameterSpec(iv);

		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));

		
		return encryptedData;

	}

	public static String decryptDES(byte[] encryptedData, String decryptKey)
			throws Exception {
		byte[] iv = decryptKey.getBytes();

		IvParameterSpec zeroIv = new IvParameterSpec(iv);

		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF-8"),
				"DES");

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);

		byte decryptedData[] = cipher.doFinal(encryptedData);

		String decryptedString = new String(decryptedData, "UTF-8");

		return decryptedString;

	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	//例子
	public static void main(String[] args) throws Exception {

		String plainText = "ghw00@fe.shenj.com 123456";

		String keyText = "2172de2e-46af-4c80-b7a8-d5f6f51784a5";

		// MessageDigest md = MessageDigest.getInstance("MD5");
		// md.update(keyText.getBytes());
		// byte b[] = md.digest();
		//
		// cutOutByte(b,8);

		keyText = getMD5(keyText);
		keyText = keyText.substring(0, 8);

		byte[] encryptedData = encryptDES(plainText, keyText);

		String decryptedString = decryptDES(encryptedData, keyText);

		String cipherText = parseByte2HexStr(encryptedData);

		System.out.println("明文：" + plainText);

		System.out.println("密钥：" + keyText);

		System.out.println("密文 16进制 编码：" + cipherText);

		System.out.println("解密后：" + decryptedString);

	}

	public static String getMD5(String str) {
		if (str == null) {
			return null;
		}
		byte[] source = str.getBytes();
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			char[] c = new char[tmp.length * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < tmp.length; i++) {
				byte byte0 = tmp[i]; // 取第 i 个字节
				c[k++] = binaryToHex(byte0 >>> 4 & 0xf); // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				c[k++] = binaryToHex(byte0 & 0xf); // 取字节中低 4 位的数字转换
			}
			s = new String(c); // 换后的结果转换为字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static char binaryToHex(int binary) {
		return hexDigits[binary];
	}

	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

}
