package com.inventario.ordencompra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrdenCompraApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdenCompraApplication.class, args);
	}

}
