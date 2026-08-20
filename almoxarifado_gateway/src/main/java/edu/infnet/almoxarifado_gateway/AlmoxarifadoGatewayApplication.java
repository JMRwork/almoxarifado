package edu.infnet.almoxarifado_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AlmoxarifadoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlmoxarifadoGatewayApplication.class, args);
	}

}
