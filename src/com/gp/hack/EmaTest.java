package com.gp.hack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.TextUtils;

import com.google.gson.Gson;
import com.google.network.BasicNetwork;
import com.google.network.HttpStack;
import com.google.network.HurlStack;
import com.google.network.NetworkResponse;
import com.google.network.ProtoRequest;
import com.google.network.Request;
import com.google.network.RequestEntity;

public class EmaTest {

	public static void main(String[] args){
	
//		String token = getEMALoginToken();
//		
//		releaseAllPhoneNumber(token);
//		
//		String phoneNumber = getEMAPhoneNumber(token);
//		
//		phoneNumber = phoneNumber.substring(0,phoneNumber.length()-1);
//		
//		sendSMS(token,phoneNumber,"121212");
//		
//	
//		
//		int tryNumber = 0;
//		while(true){
//			tryNumber++;
//			try{
//				Thread.sleep(5000);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			String result = getCode(token);
//			if(!TextUtils.isEmpty(result)){
//				break;
//			}
//			if(tryNumber>12){
//				System.out.println("尝试次数过多");
//				break;
//			}
//		}
		
//		String result ="MSG&133&13075765945&【Google】“G-179452”是您的 Google 验证码。[End]";
//		int index = result.indexOf("G-");
//		System.out.println(index);
//		String code = result.substring(index+2, index+8);
//		 
////        boolean isContains = result.matches(".*(G-[0-9]{6}).*");
////        
////        Pattern pattern = Pattern.compile(".*([0-9]{6}).*");
////        Matcher matcher = pattern.matcher(result);
////        while(matcher.find()){
////        	System.out.println(matcher.group(0));
////        }
//		System.out.println(code);
		
		getAccountInfoFromEmail("AmyRodriguez56009@gmail.com");
		
	}
	
	
	public static Account_tmp getAccountInfoFromEmail(String email){
		Account_tmp account = null;
		
	        String uploadUrl = "http://amigov.com/gmail/account/getByEmail/?email=%s";
	        uploadUrl = String.format(uploadUrl, email);
	        RequestEntity<Void> entity = new RequestEntity<Void>(uploadUrl, Request.Method.GET,Void.class,new HashMap<>(),null);
	        ProtoRequest<Void> request = new ProtoRequest<>(entity);
	        HttpStack httpStack = new HurlStack();
	        ((HurlStack)httpStack).setUseProxy(false);
	        BasicNetwork network = new BasicNetwork(httpStack);
	        try {
	            NetworkResponse response = network.performRequest(request);
	            String res = new String(response.data);
	            System.out.println("changePsd.result:"+res);
	            Gson gson = new Gson();
	            account = gson.fromJson(res, Account_tmp.class);
	            System.out.println(account.password);
	            
	        }catch (Exception e){
	            e.printStackTrace();
	        }
		return account;
	}
	
	
	class Account_tmp implements Serializable{
		public String email;
		public String recPhpne;
		public String recEmail;
		public String password;
	}
	
	public static String  getEMALoginToken(){
		String token = null;
		String url = "http://api.ema6.com:20161/Api/userLogin?uName=mosida&pWord=jianqiao098&Developer=oWN%2fsx%2b%2fFF370FKOaZakpg%3d%3d&Code=UTF8";
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            String result  = new String(response.data);
            if(!TextUtils.isEmpty(result)){
            	String[] array = result.split("&");
            	token = array[0];
            }
            System.out.println("登录ema令牌："+token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
	}
	
	public static String getEMAPhoneNumber(String token){
		String phoneNumber = null;
		if(TextUtils.isEmpty(token)){
			System.out.println("获取token失败");
		}
		String url = "http://api.ema6.com:20161/Api/userGetPhone?ItemId=133&token=%s&PhoneType=0&Code=UTF8";
		url = String.format(url, token);
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            phoneNumber = new String(response.data);
            System.out.println("获取电话是："+phoneNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
		return phoneNumber;
	}
	
	public static void sendSMS(String token,String phoneNumber,String message){
		String url = "http://api.ema6.com:20161/Api/userSendMessage?token=%s&Phone=%s&ItemId=133&Msg=%s&Code=UTF8";
		url = String.format(url, token,phoneNumber,message);
		System.out.println(url);
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            String result = new String(response.data);
            System.out.println("发送结果是："+result);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	public static String  getCode(String token){
		String result = null;
		String url = "http://api.ema6.com:20161/Api/userGetMessage?token=%s&Code=UTF8";
		url = String.format(url, token);
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            result = new String(response.data);
            if(!TextUtils.isEmpty(result) && result.equals("Null")){
            	result = null;
            }
            System.out.println("获取电话是："+result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
	}
	
	public static void releasePhoneNumber(String token,String phone){
		String url = "http://api.ema6.com:20161/Api/userReleasePhone?token=%s&phoneList=%s-133";
		url = String.format(url, token,phone);
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            String result = new String(response.data);
            if(!TextUtils.isEmpty(result) && result.equals("Null")){
            	result = null;
            }
            System.out.println("获取电话是："+result);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void releaseAllPhoneNumber(String token){
		String url = "http://api.ema6.com:20161/Api/userReleaseAllPhone?token=%s";
		url = String.format(url,token);
		RequestEntity<Void> entity = new RequestEntity<Void>(url,Request.Method.GET,Void.class,new HashMap(),null);
        ProtoRequest<Void> request = new ProtoRequest<>(entity);
        HttpStack httpStack = new HurlStack();
        ((HurlStack)httpStack).setUseProxy(false);
        BasicNetwork network = new BasicNetwork(httpStack);
        try {
            NetworkResponse response = network.performRequest(request);
            String result = new String(response.data);
            if(!TextUtils.isEmpty(result) && result.equals("Null")){
            	result = null;
            }
            System.out.println("获取电话是："+result);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
}
