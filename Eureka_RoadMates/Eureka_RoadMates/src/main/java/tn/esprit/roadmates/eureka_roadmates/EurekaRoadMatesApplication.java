package tn.esprit.roadmates.eureka_roadmates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaRoadMatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaRoadMatesApplication.class, args);
    }

}
