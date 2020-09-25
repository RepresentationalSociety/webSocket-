package com.zm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.zm.netty.service.NettyServer;

@SpringBootApplication
@EnableAsync
public class ApplicationNetty {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(ApplicationNetty.class, args);
		NettyServer netty=new NettyServer();
		try {
			netty.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
