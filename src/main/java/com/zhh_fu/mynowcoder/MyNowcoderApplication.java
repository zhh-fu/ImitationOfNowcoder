package com.zhh_fu.mynowcoder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = { "com.zhh_fu.mynowcoder.dao" })
public class MyNowcoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNowcoderApplication.class, args);
		System.out.println("Hello nowcoder");
	}

}
