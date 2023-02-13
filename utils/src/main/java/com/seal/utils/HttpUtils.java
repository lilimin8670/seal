package com.seal.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class HttpUtils {

	/**
	 * 接口调用 GET
	 */
	public static StringBuilder httpURLConectionGET(String GRT_URL,String cookies) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			URL url = new URL(GRT_URL);    // 把字符串转换为URL请求地址
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
			if(cookies!=null){
				connection.setRequestProperty("Cookie", cookies);
			}
			connection.connect();// 连接会话
			// 获取输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {// 循环读取流
				sb.append(line);
			}
			stringBuilder.append(sb);
			br.close();// 关闭流
			connection.disconnect();// 断开连接

//			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("失败!");
		}
		return stringBuilder;
	}

	/**
	 * 接口调用  POST
	 */
	public static StringBuilder httpURLConnectionPOST (String POST_URL, JSONObject param) {
		StringBuilder sb = new StringBuilder(); // 用来存储响应数据
		try {
			URL url = new URL(POST_URL);
			// 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
			connection.setConnectTimeout(300000);//连接主机超时时间
			connection.setReadTimeout(300000);//从主机读取数据超时时间
			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
			connection.setDoOutput(true);
			// 设置连接输入流为true
			connection.setDoInput(true);
			// 设置请求方式为post
			connection.setRequestMethod("POST");
			// post请求缓存设为false
			connection.setUseCaches(false);
			// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setInstanceFollowRedirects(true);
			// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
			// application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
			// ;charset=utf-8 必须要，不然会出现乱码【★★★★★】
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			if(param.getString("cookies") != null){
				connection.setRequestProperty("Cookie", param.getString("cookies"));
			}
			if(param.getString("Referer") != null){
				connection.setRequestProperty("Referer", param.getString("Referer"));
			}
			// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());

			// 将参数输出到连接
			dataout.write(param.toString().getBytes());

			// 输出完成后刷新并关闭流
			dataout.flush();
			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)

//            System.out.println(connection.getResponseCode());

			// 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			// 循环读取流,若不到结尾处
			while ((line = bf.readLine()) != null) {
//                sb.append(bf.readLine());
				sb.append(line).append(System.getProperty("line.separator"));
			}
			bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
			connection.disconnect(); // 销毁连接
		}catch (UnknownHostException e){
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用接口【"+POST_URL+"】出现错误");
		}
		return sb;
	}

	/**
	 * 接口调用  POST
	 */
	public static String httpURLConnectionPOSTForEvents (String POST_URL, JSONObject param) {
		StringBuilder sb = new StringBuilder(); // 用来存储响应数据
		try {
			URL url = new URL(POST_URL);
			// 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
			connection.setConnectTimeout(30000);//连接主机超时时间
			connection.setReadTimeout(30000);//从主机读取数据超时时间
			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
			connection.setDoOutput(true);
			// 设置连接输入流为true
			connection.setDoInput(true);
			// 设置请求方式为post
			connection.setRequestMethod("POST");
			// post请求缓存设为false
			connection.setUseCaches(false);
			// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setInstanceFollowRedirects(true);
			// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
			// application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
			// ;charset=utf-8 必须要，不然会出现乱码【★★★★★】
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			if(param.getString("cookies") != null){
				connection.setRequestProperty("Cookie", param.getString("cookies"));
			}
			if(param.getString("Referer") != null){
				connection.setRequestProperty("Referer", param.getString("Referer"));
			}


			// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());

			// 将参数输出到连接
			dataout.writeBytes(param.toString());

			// 输出完成后刷新并关闭流
			dataout.flush();
			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
            System.out.println(connection.getResponseCode());
			// 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			// 循环读取流,若不到结尾处
			while ((line = bf.readLine()) != null) {
//                sb.append(bf.readLine());
				sb.append(line).append(System.getProperty("line.separator"));
			}
			bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
			connection.disconnect(); // 销毁连接
		}catch (UnknownHostException e){
			e.printStackTrace();
			return "UnknownHostException";
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("调用接口【"+POST_URL+"】出现错误");
//			e.printStackTrace();   //20190201 调用接口失败 异常在event应用抛出
		}
		return sb.toString();
	}

	/**
	 * https的post请求
	 * @param url 路径
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsPost(String url, JSONObject param) throws Exception {
		//忽略SSL
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		//客户端
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setProtocolVersion(HttpVersion.HTTP_1_0);
			httpPost.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			//设置请求头
			httpPost.addHeader("Content-Type", "application/json");
			if(param.getString("cookies") != null){
				httpPost.addHeader("Cookie", param.getString("cookies"));
			}
			if(param.getString("Referer") != null){
				httpPost.addHeader("Referer", param.getString("Referer"));
			}
			//请求参数
			if(param != null){
				httpPost.setEntity(new StringEntity(JSONObject.toJSONString(param)));
			}
			//设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
			httpPost.setConfig(requestConfig);
			//发送请求，响应结果
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			//响应状态
			if (HttpStatus.SC_OK != statusCode) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode+"********"+JSONObject.toJSONString(param));
			}
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);        //按照官方文档的说法：二者都释放了才可以正常的释放链接
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出现异常：" + e);
		}finally {
			if(response!=null){
			response.close();
			}
			if(httpClient!=null){
			httpClient.close();
			}
		}
		return responseContent;
	}


	/**
	 * https的get请求 添加
	 * @param url 路径
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsGet(String url, JSONObject param) throws Exception {
		//忽略SSL
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		//客户端
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
			httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			//设置请求头
			httpGet.addHeader("Content-Type", "application/json");
			if(param.getString("cookies") != null){
				httpGet.addHeader("Cookie", param.getString("cookies"));
			}
			if(param.getString("Referer") != null){
				httpGet.addHeader("Referer", param.getString("Referer"));
			}
			//设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
			httpGet.setConfig(requestConfig);
			//发送请求，响应结果
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			//响应状态
			if (HttpStatus.SC_OK != statusCode) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);        //按照官方文档的说法：二者都释放了才可以正常的释放链接
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出现异常：" + e);
		}finally {
			if(response!=null){
				response.close();
			}
			if(httpClient!=null){
				httpClient.close();
			}
		}
		return responseContent;
	}

	/**
	 * 这个是 调用存储过程，速度优化
	 * @param url
	 * @param param
	 * @return
	 */
	public static String httpPOST(String url, JSONObject param) {
		try {
			URL postUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			String content = "processId=" + param.get("processId") + "&userName=" + param.get("userName") + "&passwd=" + param.get("passwd")+ "&isGenerateInstance=Y&params=" + param.get("params");
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder sb = new StringBuilder(); // 用来存储响应数据
			while ((line = reader.readLine()) != null){
				sb.append(line).append(System.getProperty("line.separator"));
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
