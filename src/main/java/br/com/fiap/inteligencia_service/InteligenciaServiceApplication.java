package br.com.fiap.inteligencia_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@EnableFeignClients
public class InteligenciaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InteligenciaServiceApplication.class, args);
	}

}
