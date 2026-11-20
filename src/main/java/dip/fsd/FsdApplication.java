package dip.fsd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FsdApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FsdApplication.class, args);

        packetcapture.capture();
    }
}