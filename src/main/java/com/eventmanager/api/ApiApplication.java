package com.eventmanager.api; // Certifique-se de que este é o seu pacote raiz correto

import io.github.cdimascio.dotenv.Dotenv; // 1. Importe a biblioteca Dotenv
import io.github.cdimascio.dotenv.DotenvEntry; // Importe DotenvEntry se for usar o forEach com tipo explícito
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	// ApiApplication.main
	public static void main(String[] args) {
		// Supondo que seu arquivo se chame ".env" e esteja no diretório de trabalho
		Dotenv dotenv = Dotenv.configure().filename(".env").ignoreIfMissing().load();

		if (dotenv != null) {
			String dbPassword = dotenv.get("SPRING_DATASOURCE_PASSWORD");
			if (dbPassword != null) {
				System.setProperty("SPRING_DATASOURCE_PASSWORD", dbPassword);
				System.out.println("System Property 'SPRING_DATASOURCE_PASSWORD' set from .env file.");
				// Para depuração EXTREMA (não deixe em produção):
				// System.out.println("Password from .env: [" + dbPassword + "]");
			} else {
				System.out.println("WARNING: SPRING_DATASOURCE_PASSWORD not found in .env file.");
			}

			// Carregue outras variáveis do .env se necessário, como AWS_BUCKETNAME
			String bucketName = dotenv.get("AWS_BUCKETNAME");
			if (bucketName != null) {
				System.setProperty("AWS_BUCKETNAME", bucketName); // Ou a propriedade que seu app espera
				System.out.println("System Property for bucket name set from .env file.");
			}

		} else {
			System.out.println("WARNING: .env file could not be loaded.");
		}

		SpringApplication.run(ApiApplication.class, args);
	}
}