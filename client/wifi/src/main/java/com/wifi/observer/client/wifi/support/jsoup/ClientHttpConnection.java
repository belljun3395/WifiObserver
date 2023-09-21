package com.wifi.observer.client.wifi.support.jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

// todo 필요한 것이 생기면 추가할 것
public class ClientHttpConnection extends HttpConnection implements Connection {

	public static ClientHttpConnection connect(String url) {
		ClientHttpConnection con = new ClientHttpConnection();
		con.url(url);
		return con;
	}

	public static ClientHttpConnection connect(URL url) {
		ClientHttpConnection con = new ClientHttpConnection();
		con.url(url);
		return con;
	}

	@Override
	public ClientDocument get() throws IOException {
		return ClientDocument.of(super.get());
	}

	@Override
	public ClientDocument post() throws IOException {
		return ClientDocument.of(super.post());
	}

	@Override
	public ClientHttpConnection userAgent(String userAgent) {
		super.userAgent(userAgent);
		return this;
	}

	@Override
	public ClientHttpConnection headers(Map<String, String> headers) {
		super.headers(headers);
		return this;
	}

	@Override
	public ClientHttpConnection data(Map<String, String> data) {
		super.data(data);
		return this;
	}

	@Override
	public ClientHttpConnection timeout(int millis) {
		super.timeout(millis);
		return this;
	}

	@Override
	public ClientHttpConnection cookie(String name, String value) {
		super.cookie(name, value);
		return this;
	}
}
