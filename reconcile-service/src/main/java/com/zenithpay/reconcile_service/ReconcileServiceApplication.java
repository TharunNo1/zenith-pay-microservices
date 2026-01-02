package com.zenithpay.reconcile_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(
        exclude={
            org.springframework.boot.batch.autoconfigure.BatchAutoConfiguration.class,
        }
        )
@EnableScheduling
@EnableDiscoveryClient
public class ReconcileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReconcileServiceApplication.class, args);
	}

}
