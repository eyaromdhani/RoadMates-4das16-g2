package tn.esprit.autoloc.fidelite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class FideliteApplication {

    public static void main(String[] args) {
        SpringApplication.run(FideliteApplication.class, args);
    }

}
