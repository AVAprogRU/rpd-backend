package com.suai.department43.loutsker.rpddrafter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RpdDrafterApplication {
	public static void main(String[] args) {
		SpringApplication.run(RpdDrafterApplication.class, args);
	}
}
