package com.gp.hack;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
public class AESCipher {

	/**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 模式/填充
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	public static byte[] encrypt(byte[] data, byte[] keyData, byte[] ivData)
            throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyData, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivData);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, byte[] keyData, byte[] ivData)
            throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyData, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivData);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }


    private static final String TAG = "AESCrypt";

    //AESCrypt-ObjC uses CBC and PKCS7Padding
    private static final String AES_MODE = "AES/CBC/PKCS7Padding";
    private static final String CHARSET = "UTF-8";
    private static byte[] secretKeySpec;
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00};

    //togglable log option (please turn off in live!)
    public static boolean DEBUG_LOG_ENABLED = true;


    private static byte[] generateKey(final String key) {

        byte[] secretKeySpec = new byte[16];
        byte[] keyBytes = key.getBytes();
        for (int i = 0; i < 16; i++) {
            secretKeySpec[i] = keyBytes[i];
        }

        return secretKeySpec;
    }


    public static String encrypt(final String key, final String message) throws GeneralSecurityException,
            UnsupportedEncodingException {
        try {
            if(DEBUG_LOG_ENABLED) {
                log("message", message);
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(generateKey(key), "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encrypted = c.doFinal(message.getBytes(CHARSET));
            if(DEBUG_LOG_ENABLED) {
                log("encrypted", encrypted);
            }
            byte[] result = Arrays.copyOf(ivBytes, ivBytes.length + encrypted.length);
            System.arraycopy(encrypted, 0, result, ivBytes.length, encrypted.length);
            String encoded = Base64.encodeToString(result, Base64.NO_WRAP);
            if(DEBUG_LOG_ENABLED) {
                log("Base64.NO_WARP", encoded);
            }
            return encoded;
        } catch (GeneralSecurityException e) {
            System.out.println("GeneralSecurityException :"+e);
            throw new GeneralSecurityException(e);
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException :"+e);
            throw new UnsupportedEncodingException();
        }
    }


    public static String decrypt(final String key, String base64EncodedCipherText,boolean crypt) throws GeneralSecurityException,
            UnsupportedEncodingException {
        if (!crypt) return base64EncodedCipherText;
        try {
            final SecretKeySpec secretKey = new SecretKeySpec(generateKey(key), "AES");
            if(DEBUG_LOG_ENABLED) {
                log("base64EncodedCipherText", base64EncodedCipherText);
            }
            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);

            byte[] iv = Arrays.copyOfRange(decodedCipherText, 0, 16);
            byte[] json = Arrays.copyOfRange(decodedCipherText, 16, decodedCipherText.length);
            if(DEBUG_LOG_ENABLED) {
                log("decodedCipherText", decodedCipherText);
            }
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            c.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptStr = c.doFinal(json);
            if(DEBUG_LOG_ENABLED) {
                log("decryptStr", decryptStr);
            }
            return new String(decryptStr, CHARSET);
        } catch (GeneralSecurityException e) {
            log("GeneralSecurityException", e.toString());
            throw new GeneralSecurityException(e);
        } catch (UnsupportedEncodingException e) {
            log("UnsupportedEncodingException", e.toString());
            throw new UnsupportedEncodingException();
        }
    }


    private static void log(String what, byte[] bytes) {
        if (DEBUG_LOG_ENABLED)
            System.out.print(what + "[" + bytes.length + "] [" + bytesToHex(bytes) + "]");
    }

    private static void log(String what, String value) {
        if (DEBUG_LOG_ENABLED)
            System.out.println(what + "[" + value.length() + "] [" + value + "]");
    }


    /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     *
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
