package br.com.devtt;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class DevTtApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevTtApplication.class, args);
	}
}