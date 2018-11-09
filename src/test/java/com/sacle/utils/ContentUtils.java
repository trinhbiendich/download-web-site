package com.sacle.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
	
	public static void main(String[] args) throws IOException {
		download("https://chugai-pharm.jp/pr/drug/pdf/drug_hip_code.pdf", new File("/media/TOOLS/sample.pdf"));
	}
	
	public static void download(final String url, final File destination) throws IOException {
		final URLConnection connection = new URL(url).openConnection();
		connection.setConnectTimeout(60000);
		connection.setReadTimeout(60000);
		connection.addRequestProperty("User-Agent", "Mozilla/5.0");
		final FileOutputStream output = new FileOutputStream(destination, false);
		final byte[] buffer = new byte[2048];
		int read;
		final InputStream input = connection.getInputStream();
		while ((read = input.read(buffer)) > -1){
			System.out.println(read);
			output.write(buffer, 0, read);
		}
		output.flush();
		output.close();
		input.close();
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
