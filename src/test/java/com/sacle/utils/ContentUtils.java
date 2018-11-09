package com.sacle.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ContentUtils {
	
	public static byte[] urlHttpsToBytes(String url) {
		byte[] result = null;
		try {
			// Create a new trust manager that trust all certificates
			TrustManager[] trustAllCerts = new TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};

			// Activate the new trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			}
			URL uri = new URL(url);
			HttpURLConnection httpcon = (HttpURLConnection) uri.openConnection(); 
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.76"); 
			System.setProperty("http.agent", "Chrome");
			InputStream in = httpcon.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			result = out.toByteArray();
		} catch (Exception ex) {
			System.out.println("eee : " + ex.getMessage());
		}
		return result;
	}
	
	public static byte[] urlToBytes(String url) {
		byte[] result = null;
		try {
			URL uri = new URL(url);
			InputStream in = new BufferedInputStream(uri.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			result = out.toByteArray();
		} catch (Exception ex) {
			System.out.println("eee : " + ex.getMessage());
		}
		return result;
	}
}
