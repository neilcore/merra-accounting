package org.merra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaRepositories(basePackages = {"org.merra"})
@EntityScan(basePackages = {"org.merra"})
@SpringBootApplication(scanBasePackages = {"org.merra"})
@RestController
@RequestMapping
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @GetMapping
    public String defaultLandingPage() {
    	return "Merra accounting API is running...";
    }
}
