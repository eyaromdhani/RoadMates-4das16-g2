package tn.esprit.autoloc.incidents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IncidentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncidentsApplication.class, args);
    }

}
