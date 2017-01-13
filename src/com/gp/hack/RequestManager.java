package com.gp.hack;

import java.net.URLEncoder;
import java.util.Map;

public class RequestManager {

	
	public static final boolean DEBUG = true;
    protected static final String DEFAULT_PARAMS_ENCODING = "UTF-8";


    /**
     * 只供内部调用，计算参数MD5时不适用ENCODE
     *
     * @param
     * @param params
     * @return
     */
    public static String getAuthStr(String key,Map<String, String> params) {
        try {
            String cryptStr = null;
            if (params != null) {
                StringBuilder encodedParams = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), DEFAULT_PARAMS_ENCODING));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(), DEFAULT_PARAMS_ENCODING));
                    encodedParams.append('&');
                }
                if(DEBUG) {
                    System.out.println(encodedParams.toString());
                }
                cryptStr = AESCipher.encrypt(key, encodedParams.toString());
            }
            return cryptStr;
        } catch (Throwable e) {
            if (DEBUG) {
                System.out.println(e);
            }
        }
        return "";
    }
}
