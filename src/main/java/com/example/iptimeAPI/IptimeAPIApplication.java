package com.example.iptimeAPI;

import com.example.iptimeAPI.util.iptime.Iptime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class IptimeAPIApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(IptimeAPIApplication.class, args);
		Iptime iptime = new Iptime();
		String cookieValue = iptime.getCookieValue();
		iptime.login(cookieValue);
		iptime.getList(cookieValue);

	}

}
