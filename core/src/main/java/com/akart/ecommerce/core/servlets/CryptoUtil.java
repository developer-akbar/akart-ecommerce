package com.akart.ecommerce.core.servlets;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class which provides methods to encrypt and decrypt a string using
 * AES
 */
public class CryptoUtil {

	private static final String ALGORITHM = "AES"; // Default uses ECB PKCS5Padding

	private static final String KEY_SECRET = "2s5v8y/B?E(H+MbQ";

	/**
	 * This encrypts the given string
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data) throws Exception {
		String encodedBase64Key = encodeKey(KEY_SECRET);
		Key key = generateKey(encodedBase64Key);
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		String encryptedValue = Base64.getEncoder().encodeToString(encVal);
		return encryptedValue;
	}

	/**
	 * This decrypts the given encrypted String
	 * 
	 * @param strToDecrypt
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String strToDecrypt) throws Exception {
		String encodedBase64Key = encodeKey(KEY_SECRET);
		Key key = generateKey(encodedBase64Key);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

	}

	/**
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	private static Key generateKey(String secret) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
		Key key = new SecretKeySpec(decoded, ALGORITHM);
		return key;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String decodeKey(String str) {
		byte[] decoded = Base64.getDecoder().decode(str.getBytes());
		return new String(decoded);
	}

	/**
	 * @param str
	 * @return
	 */
	public static String encodeKey(String str) {
		byte[] encoded = Base64.getEncoder().encode(str.getBytes());
		return new String(encoded);
	}
}