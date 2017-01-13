package com.gp.hack;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.gson.Gson;
import com.google.network.BasicNetwork;
import com.google.network.HttpStack;
import com.google.network.HurlStack;
import com.google.network.NetworkResponse;
import com.google.network.ProtoRequest;
import com.google.network.Request;
import com.google.network.RequestEntity;

public class GpRankingTest {

	private WebDriver driver = null;
	private static List<Account> list = new ArrayList<Account>();
	public static String result;
	public static List<String> accountList;
    static{
    	accountList = loadTestInfo2();
    	for(int i = 0;i<accountList.size();i++){
    		Account account = new Account();
    		String info = accountList.get(i);
    		String[] infos = info.split(",");
    		account.email = infos[0];
    		account.gid = Integer.valueOf(infos[1]);
    		account.pwd = infos[2];
    		account.rec_email="1934316955@qq.com";
    		list.add(account);
    	}
    
//    	Gson gson = new Gson();
//    	 Account[] accounts = gson.fromJson(result, Account[].class);
//         List<Account> testList = Arrays.asList(accounts);
//         for(int i=0;i<testList.size();i++){
//        	 list.add(testList.get(i));
//        	 Account account = list.get(i);
////        	 writeFile("./successAccount10",account.email+"\n",true);
//// 			writeFile("/Users/apple/javaproject/GpHack/src/com/gp/hack/successAccountGid10",(account.gid-1)+"\n",true);
////        	 writeFile("./gidss",(account.gid-1)+"\n",true);
//         }
    }
    
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    public  static String loadTestInfos() {
     
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = GpRankingTest.class.getResourceAsStream("Accounts.json");
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            int index = 0;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
              
                 sb.append(line);
            }
           
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<String> loadTestInfo2(){
    	List<String> list = new ArrayList<>();
    	 InputStream is = null;
         BufferedReader reader = null;
         try {
             is = GpRankingTest.class.getResourceAsStream("account");
             reader = new BufferedReader(new InputStreamReader(is));
             String line = "";
             int index = 0;
             StringBuilder sb = new StringBuilder();
             while ((line = reader.readLine()) != null) {
            	 list.add(line);
                 
             }
            
             return list;
         }catch (Exception e){
             e.printStackTrace();
         }
         return null;
    }
    
//	private String email="BrendaMiller29876@gmail.com";
//	private String pwd="DorothyMiller24192";
	//private String url = "http://www.baidu.com";
	//每个用例执行前会执行该方法
	@Before
	public void startUp(){

	}
	//每个用例执行后会执行该方法
	@After
	public void tearDown(){
	//退出操作
	
	}
	
	public static final String AES_KEY = "3Ce7671Ff686D51d";
	
	class Result{
        int c = 0;
    }
	
	public void changeInfomation(Account account){
        Map<String,String> map = new HashMap<>();
        map.put("email",account.email);
        map.put("pwd",account.pwd);
        map.put("recEmail",account.rec_email);
        map.put("available","1");
        map.put("reason","change password");
        String params = RequestManager.getAuthStr(AES_KEY, map);
        String uploadUrl = "http://amigov.com/gmail/account/update?s="+params;
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
            Result lt = gson.fromJson(res,Result.class);
            if(lt != null && lt.c == 0){
            	System.out.println("数据库更新信息成功");
            }else{
            	System.out.println("数据库更新信息失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	// 参考这篇文章轻松搞定模拟测试
//	http://www.cnblogs.com/qingchunjun/p/4208159.html
		
	
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
            System.out.println("获取验证码是"+result);
            //MSG&133&13075765945&【Google】“G-179452”是您的 Google 验证码。[End]
//            result.matches("([0-9]{6})");
            
            
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
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
            System.out.println("释放所有电话号码："+result);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void releasePhoneNumber(String token,String phone){
		String url = "http://api.ema6.com:20161/Api/userReleasePhone?token=%s&phoneList=%s-133&Code=UTF8";
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
            System.out.println("解除电话"+result);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public Account_tmp getAccountInfoFromEmail(String email){
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
	            
	        }catch (Exception e){
	            e.printStackTrace();
	        }
		return account;
	}
	
	@Test
	public void startTest(){
		for(int i=0;i<list.size();i++){
			Account account = list.get(i);
	//打开新窗口
			try{
				System.setProperty("webdriver.chrome.driver", "chromedriver");
				
				driver = new ChromeDriver();
				driver.get("https://www.google.com.tw/?gfe_rd=cr&ei=uNDmV8mGPIyB0ATvmLKwAQ");
				//https://www.google.com.sg/?gws_rd=ssl
				driver.findElement(By.linkText("登录")).click();
				sleepQuick();
				driver.findElement(By.id("Email")).clear();
				driver.findElement(By.id("Email")).sendKeys(account.email);
				driver.findElement(By.id("next")).click();
				sleepQuick();	
				driver.findElement(By.id("Passwd")).clear();
				driver.findElement(By.id("Passwd")).sendKeys(account.pwd);
				driver.findElement(By.id("signIn")).click();
				
				sleepQuick();
				//等待10秒钟，如果还是能够采集到该界面,说明密码有问题
				try{
					System.out.println("等待密码验证");
					Thread.sleep(3000);
				}catch(Exception e){
					e.printStackTrace();
				}
				Account_tmp account_tmp = getAccountInfoFromEmail(account.email);
				String password = account.pwd;
				try{
					WebElement pwdElement = driver.findElement(By.xpath(".//*[@role='alert' and @class='error-msg']"));
					System.out.println("帐号密码错误");
					if(pwdElement != null){
						try{
							password = account_tmp.password;
							System.out.println("尝试数据库密码:"+password);
							WebElement element = driver.findElement(By.id("Passwd"));
							if(element != null){
								element.clear();
								driver.findElement(By.id("Passwd")).sendKeys(password);
								driver.findElement(By.id("signIn")).click();
							}
							//再等2秒钟，如果还出现错误，那尝试用33338888；
							try{
								System.out.println("等待密码验证");
								Thread.sleep(2000);
							}catch(Exception e){
								e.printStackTrace();
							}
							
							password="33338888";
							
							try{
								WebElement pwdElement1 = driver.findElement(By.xpath(".//*[@role='alert' and @class='error-msg']"));
								if(pwdElement1!=null){
									WebElement element1 = driver.findElement(By.id("Passwd"));
									if(element1 != null){
										element1.clear();
										driver.findElement(By.id("Passwd")).sendKeys(password);
										driver.findElement(By.id("signIn")).click();
									}
									account.pwd = password;
								}else{
									System.out.println("密码登录成功");
								}
							}catch(Exception e){
//								e.printStackTrace();
							}
							
						}catch(Exception e){
//							e.printStackTrace();
							System.out.println("帐号登录失败，测试服务器密码和38");
						}
					}
				}catch(Exception e){
//					e.printStackTrace();
//					System.out.println("未知错误");
				}
				sleepQuick();
				
				//有可能要确认辅助邮箱，输入平常登录所在的城市，
				try{
					System.out.println("需要确认填入辅助邮箱");
					String rec_email = account_tmp.recEmail;
					WebElement element = driver.findElement(By.xpath(".//*[text()='确认您的辅助邮箱']"));
					if(element!=null){
						element.click();
						WebElement element1 = driver.findElement(By.xpath(".//*[@name='email' and @type='email']"));
						if(element1!=null){
							System.out.println("填入辅助邮箱:"+rec_email);
							if(!TextUtils.isEmpty(rec_email)){
								element1.sendKeys(rec_email);
								WebElement element2 = driver.findElement(By.xpath(".//*[@type='submit' and @id='submit']"));
								element2.click();
								System.out.println("点击完成");
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
				boolean needVerifyNumber = false;
				String phoneNumber = null;
			    String token = null;
			    
				try{
					System.out.println("判断是否需要电话验证");
					WebElement verifyNumber = driver.findElement(By.xpath(".//*[@id='phoneNumber' and @name='phoneNumber']"));
					if(verifyNumber != null){
						System.out.println("需要电话验证");
						needVerifyNumber = true;
					    boolean hasGetPhoneNumber = false;
					    int getPhoneNumber = 0;
					    while(!hasGetPhoneNumber){
					    	getPhoneNumber++;
					    	if(getPhoneNumber > 5){
					    		System.out.println("获取电话超过5次，退出");
					    		driver.quit();
					    		continue;
					    	}
					    	System.out.println("获取电话号码"+getPhoneNumber);
					    	token = getEMALoginToken();
					    	phoneNumber = getEMAPhoneNumber(token);
					    	if(!TextUtils.isEmpty(phoneNumber)){
						    	hasGetPhoneNumber = true;
						    }
					    }
					    
					    if(TextUtils.isEmpty(phoneNumber)){
					    	System.out.println("获取电话为空，退出");
				    		driver.quit();
				    		continue;
					    }
					    phoneNumber = phoneNumber.substring(0,phoneNumber.length()-1);
					    System.out.println("获取电话号码是:"+phoneNumber);
					    
					    verifyNumber.sendKeys("+86"+phoneNumber);
					    
					    System.out.println("点击提交电话号码");
					    driver.findElement(By.xpath(".//*[@type='submit' and @id='submitSms']")).click();
					    
					    sleepQuick();
					    sleepQuick();
					    System.out.println("获取验证码");
					    
					    WebElement verifyCode = driver.findElement(By.xpath(".//*[@type='tel' and @id='pin']"));
					    if(verifyCode != null){
					    	if(TextUtils.isEmpty(token) || TextUtils.isEmpty(phoneNumber)){
					    		System.out.println("短信信息有误，退出"); 
					    		driver.quit();
					    		continue;
					    	}
					    	
					    	String code = null;
					    	System.out.println("等待收到短信");
					    	int tryNumber = 0;
					    	String result = null;
							while(true){
								tryNumber++;
								try{
									Thread.sleep(5000);
								}catch(Exception e){
									e.printStackTrace();
								}
								result = getCode(token);
								if(!TextUtils.isEmpty(result)){
									break;
								}
								if(tryNumber>6){
									System.out.println("尝试次数过多");
									System.out.println("异常情况解除号码");
									releasePhoneNumber(token,phoneNumber);
									sleepQuick();
									driver.quit();
									continue;
								}
							}
							
							System.out.println("解除号码");
							releasePhoneNumber(token,phoneNumber);
						
					        System.out.println(".............验证码是:"+result); 
					        int index = result.indexOf("G-");
					       
							code = result.substring(index+2, index+8);
							System.out.println("验证码是:"+code);
							boolean isMatch = code.matches("([0-9]{6})");
							if(!isMatch){
								System.out.println("验证码格式不匹配");
								driver.quit();
								continue;
							}
							System.out.println("填写验证码");
							verifyCode.sendKeys(code);
					        System.out.println("填写验证码完毕");
					        System.out.println("点击完成");
					        WebElement finish = driver.findElement(By.xpath(".//*[@type='submit' and @id='submit']"));
					        if(finish!=null){
					        	finish.click();
					        }
					        System.out.println("点击完成成功");
					    }else{
					    	System.out.println("上个发送验证码到手机号错误，退出");
					    	driver.quit();
					    	continue;
					    }
					}
				}catch(Exception e){
					if(!TextUtils.isEmpty(token)){
						releaseAllPhoneNumber(token);
					}
					e.printStackTrace();
					if(needVerifyNumber){
						driver.quit();
						continue;
					}
				}
				
				
				boolean needChangedPsd = false;
				//如果还是在台湾网络下，是不用电话验证的，电话验证是用于在跨网络情况下
				//em6电话是可以用于电话验证
				
				
				//在台湾网络下：
				//这里可能需要修改密码
				try{
					Haikunator haikunator = new HaikunatorBuilder().setTokenHex(true).setDelimiter("").build();
					String newPassword = haikunator.haikunate();
					System.out.println("设置密码"+newPassword);
					driver.findElement(By.xpath(".//form[@method='POST']/*/*/input[@id='Password']")).sendKeys(newPassword);
					System.out.println("确认密码");
					driver.findElement(By.xpath(".//form[@method='POST']/*/*/input[@id='ConfirmPassword']")).sendKeys(newPassword);
					System.out.println("更改密码");
					driver.findElement(By.xpath(".//form[@method='POST']/*/input[@type='submit']")).click();
					System.out.println("修改密码成功");
					account.pwd = newPassword;
					System.out.println("开始更新数据库信息");
					changeInfomation(account);
					needChangedPsd = true;
				}catch(Exception e){
					e.printStackTrace();
					needChangedPsd = false;
				}
				
				boolean needAddEmail = false;
				try {
					System.out.println("设置辅助邮箱");
					driver.findElement(By.xpath(".//*[@type='text' and @class='Kc fh Q-Sb-Q']")).sendKeys(account.rec_email);
					System.out.println("点击完成");
					driver.findElement(By.xpath(".//*[@role='button' and text()='完成']")).click();
					System.out.println("完成辅助邮箱设置");
					System.out.println("开始更新数据库信息");
					changeInfomation(account);
					needAddEmail = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(needAddEmail){
					System.out.println("重新登录");
					driver.quit();
					System.setProperty("webdriver.chrome.driver", "chromedriver");
					driver = new ChromeDriver();
					sleepQuick();
					driver.get("https://www.google.com.tw/?gfe_rd=cr&ei=uNDmV8mGPIyB0ATvmLKwAQ");
					driver.findElement(By.linkText("登录")).click();
					sleepQuick();
					driver.findElement(By.id("Email")).clear();
					driver.findElement(By.id("Email")).sendKeys(account.email);
					driver.findElement(By.id("next")).click();
					sleepQuick();	
					driver.findElement(By.id("Passwd")).clear();
					driver.findElement(By.id("Passwd")).sendKeys(account.pwd);
					driver.findElement(By.id("signIn")).click();
					sleepQuick();
				}
				
				boolean needAgree = false;
				
				try {
					sleepQuick();
					System.out.println("点击滚动按钮1");
					driver.findElement(By.xpath(".//*[@role='button' and @jscontroller='VXdfxd' ]")).click();
					sleepQuick();
					System.out.println("点击滚动按钮2");
					driver.findElement(By.xpath(".//*[@role='button' and @jscontroller='VXdfxd' ]")).click();
					sleepQuick();
					needAgree = true;
					System.out.println("点击滚动按钮3");
					StringBuilder sb = new StringBuilder();
//					sb.append(account.email).append(",").append(account.gid).append(",").append(account.pwd).append(",").append(account.region);
//					writeFile("./result",sb.toString()+"\n",true);
					driver.findElement(By.xpath(".//*[@role='button' and @jscontroller='VXdfxd' ]")).click();
					sleepQuick();
					System.out.println("点击滚动按钮4");
					driver.findElement(By.xpath(".//*[@role='button' and @jscontroller='VXdfxd' ]")).click();
				} catch (Exception er) {
					try{
						if(needAgree){
							System.out.println("点击了同意按钮");
							driver.findElement(By.xpath(".//*[@class='RveJvd snByac']")).click();
							System.out.println("整个过程完毕");
						}else{
							throw er;
						}
					}catch(Exception e){
						System.out.println("点击同意按钮时候奔溃");
						throw e;
					}
				}
				sleepSlow();
				driver.quit();
			}catch(Exception e){
				writeFile("/Users/apple/javaproject/GpHack/src/com/gp/hack/failedAccount",account.email+"\n",true);
				e.printStackTrace();
				sleepSlow();
				driver.quit();
		        continue;
			}
			writeFile("/Users/apple/javaproject/GpHack/src/com/gp/hack/successAccount",account.email+"\n",true);
//			writeFile("/Users/apple/javaproject/GpHack/src/com/gp/hack/successAccountGid",(account.gid-1)+"\n",true);
		}
	}
	
	public void sleepSlow(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sleepQuick(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

//{"email": "AmyRodriguez56009@gmail.com", "recPhpne": "15669489174", "recEmail": "1934316955@qq.com", "password": "drywood7bc7"}
class Account_tmp implements Serializable{
	public String email;
	public String recPhpne;
	public String recEmail;
	public String password;
}

class Account implements Serializable{
	 public int gid;
	    public String first_name;
	    public String last_name;
	    public String email;
	    public String andid;
	    public String gsfauth;
	    public String gsftoken;
	    public String auth;
	    public String token;
	    public String securityauth;
	    public String securitytoken;
	    public String pwd;
	    public String rec_phone;
	    public String rec_email;
	    public String ver_code;
	    public int gplus;
	    public String region;
	    public int available;
	    public String reason;
	    public String device_no;
	    public String ip;
	    public String update_ip;
	    public String create_time;
	    public String update_time;
	    public String token_create_time;

	    public int getGid() {
	        return gid;
	    }

	    public void setGid(int gid) {
	        this.gid = gid;
	    }

	    public String getToken_create_time() {
	        return token_create_time;
	    }

	    public void setToken_create_time(String token_create_time) {
	        this.token_create_time = token_create_time;
	    }

	    public String getFirst_name() {
	        return first_name;
	    }

	    public void setFirst_name(String first_name) {
	        this.first_name = first_name;
	    }

	    public String getLast_name() {
	        return last_name;
	    }

	    public void setLast_name(String last_name) {
	        this.last_name = last_name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getAndid() {
	        return andid;
	    }

	    public void setAndid(String andid) {
	        this.andid = andid;
	    }

	    public String getGsfauth() {
	        return gsfauth;
	    }

	    public void setGsfauth(String gsfauth) {
	        this.gsfauth = gsfauth;
	    }

	    public String getGsftoken() {
	        return gsftoken;
	    }

	    public void setGsftoken(String gsftoken) {
	        this.gsftoken = gsftoken;
	    }

	    public String getAuth() {
	        return auth;
	    }

	    public void setAuth(String auth) {
	        this.auth = auth;
	    }

	    public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }

	    public String getSecurityauth() {
	        return securityauth;
	    }

	    public void setSecurityauth(String securityauth) {
	        this.securityauth = securityauth;
	    }

	    public String getSecuritytoken() {
	        return securitytoken;
	    }

	    public void setSecuritytoken(String securitytoken) {
	        this.securitytoken = securitytoken;
	    }

	    public String getPwd() {
	        return pwd;
	    }

	    public void setPwd(String pwd) {
	        this.pwd = pwd;
	    }

	    public String getRec_phone() {
	        return rec_phone;
	    }

	    public void setRec_phone(String rec_phone) {
	        this.rec_phone = rec_phone;
	    }

	    public String getRec_email() {
	        return rec_email;
	    }

	    public void setRec_email(String rec_email) {
	        this.rec_email = rec_email;
	    }

	    public String getVer_code() {
	        return ver_code;
	    }

	    public void setVer_code(String ver_code) {
	        this.ver_code = ver_code;
	    }

	    public int getGplus() {
	        return gplus;
	    }

	    public void setGplus(int gplus) {
	        this.gplus = gplus;
	    }

	    public String getRegion() {
	        return region;
	    }

	    public void setRegion(String region) {
	        this.region = region;
	    }

	    public int getAvailable() {
	        return available;
	    }

	    public void setAvailable(int available) {
	        this.available = available;
	    }

	    public String getReason() {
	        return reason;
	    }

	    public void setReason(String reason) {
	        this.reason = reason;
	    }

	    public String getDevice_no() {
	        return device_no;
	    }

	    public void setDevice_no(String device_no) {
	        this.device_no = device_no;
	    }

	    public String getIp() {
	        return ip;
	    }

	    public void setIp(String ip) {
	        this.ip = ip;
	    }

	    public String getUpdate_ip() {
	        return update_ip;
	    }

	    public void setUpdate_ip(String update_ip) {
	        this.update_ip = update_ip;
	    }

	    public String getCreate_time() {
	        return create_time;
	    }

	    public void setCreate_time(String create_time) {
	        this.create_time = create_time;
	    }

	    public String getUpdate_time() {
	        return update_time;
	    }

	    public void setUpdate_time(String update_time) {
	        this.update_time = update_time;
	    }
}
