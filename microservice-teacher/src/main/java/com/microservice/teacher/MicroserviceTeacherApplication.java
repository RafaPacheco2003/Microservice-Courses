package com.microservice.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.microservice.teacher.client")
public class MicroserviceTeacherApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceTeacherApplication.class, args);
	}

}
