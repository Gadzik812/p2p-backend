package com.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
=======
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;  // ← НОВЫЙ ИМПОРТ
import org.apache.catalina.connector.Connector;

@SpringBootApplication
@EnableScheduling  // ← НОВАЯ СТРОЧКА (включает автоматическое обновление курса)
>>>>>>> origin/main
public class P2pApplication {

    public static void main(String[] args) {
        SpringApplication.run(P2pApplication.class, args);
    }

<<<<<<< HEAD
=======
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            connector.setSecure(false);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
>>>>>>> origin/main
}