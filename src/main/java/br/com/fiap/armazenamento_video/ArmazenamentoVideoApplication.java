package br.com.fiap.armazenamento_video;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class ArmazenamentoVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArmazenamentoVideoApplication.class, args);
	}

}
