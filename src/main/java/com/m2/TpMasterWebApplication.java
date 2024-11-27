package com.m2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class TpMasterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpMasterWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext ctx, DataSource dataSource) {
        return args -> {
            System.out.println("Vérification de la connexion à la DataSource...");
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Connexion réussie à la base de données !");
                System.out.println("URL de la base de données : " + connection.getMetaData().getURL());
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            }
        };
    }
}
