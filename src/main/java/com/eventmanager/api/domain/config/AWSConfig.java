package com.eventmanager.api.domain.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.region}") // Você ainda pode querer injetar a região explicitamente
    private String awsRegion;

    // NÃO PRECISA MAIS DE:
    // @Value("${aws.accessKeyId}")
    // private String accessKey;

    // @Value("${aws.secretAccessKey}")
    // private String secretKey;

    @Bean
    public AmazonS3 createS3Instance() {
        // O AmazonS3ClientBuilder encontrará as credenciais automaticamente
        // da cadeia de provedores padrão (que inclui ~/.aws/credentials).
        // Ele também pode pegar a região padrão do ~/.aws/config se awsRegion não for definido explicitamente.
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(awsRegion) // Explicitamente define a região. Se não definido, usará a região padrão do perfil ou da EC2.
                // .withCredentials(...) NÃO É MAIS NECESSÁRIO AQUI se usar a cadeia padrão
                .build();
    }
}