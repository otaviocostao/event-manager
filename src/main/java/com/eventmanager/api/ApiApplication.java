package com.eventmanager.api; // Certifique-se de que este é o seu pacote raiz correto

import io.github.cdimascio.dotenv.Dotenv; // 1. Importe a biblioteca Dotenv
import io.github.cdimascio.dotenv.DotenvEntry; // Importe DotenvEntry se for usar o forEach com tipo explícito
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();


		if (dotenv != null) {
			for (DotenvEntry entry : dotenv.entries()) {

				String springPropertyName = entry.getKey().replace("_", ".").toLowerCase();
				System.setProperty(springPropertyName, entry.getValue());


				System.setProperty(entry.getKey(), entry.getValue());
			}
		}

		SpringApplication.run(ApiApplication.class, args);
	}
}