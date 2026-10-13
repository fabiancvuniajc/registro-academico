package com.example.registroacademico.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.registroacademico.infrastructure.adapters.out.persistence")
@EntityScan("com.example.registroacademico.infrastructure.adapters.out.persistence.entities")
@ComponentScan("com.example.registroacademico")
public class RegistroAcademicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistroAcademicoApplication.class, args);
    }

}
