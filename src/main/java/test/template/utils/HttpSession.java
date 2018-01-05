package test.template.utils;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpSession {
	private static SSLContext sc;
	static {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
		} catch (Exception e) {
		}
	}
	
	private HttpClient client = HttpClients.custom().setSSLContext(sc).build();
	
	private String execute(HttpRequestBase req) throws Exception {
		HttpResponse resp = client.execute(req);
		return EntityUtils.toString(resp.getEntity());
	}

	public String get(String url) throws Exception {
		return execute(new HttpGet(url));
	}
	
	public String post(String url, String data) throws Exception {
		HttpPost request = new HttpPost(url);
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setEntity(new StringEntity(data));
		return execute(request);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new HttpSession().post("http://127.0.0.1:8000/", "as=ss"));
	}
}
