package test.template.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public class HttpUtils {
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

		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		} catch (Exception e) {
		}
	}

	public static final byte[] get(String url, Map<String, String> headers) {
		try {
			URLConnection conn = new URL(url).openConnection();
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			InputStream is = conn.getInputStream();
			byte[] result = IOUtils.toByteArray(is);
			is.close();
			List<String> header = conn.getHeaderFields().get("Content-Disposition");
			if (header != null && header.size() > 0) {
				headers.put("Content-Disposition", header.get(0));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final byte[] post(String url, String data, Map<String, String> headers) {
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setDoOutput(true);

			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(os);
			wr.write(data);
			wr.flush();
			wr.close();
			os.close();
			System.out.println("HTTP Response headers: " + conn.getHeaderFields());
			List<String> header = conn.getHeaderFields().get("Content-Disposition");
			if (header != null && header.size() > 0) {
				headers.put("Content-Disposition", header.get(0));
			}
			header = conn.getHeaderFields().get("Content-Type");
			if (header != null && header.size() > 0) {
				headers.put("Content-Type", header.get(0));
			}
			InputStream is = conn.getInputStream();
			byte[] result = IOUtils.toByteArray(is);
			is.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
