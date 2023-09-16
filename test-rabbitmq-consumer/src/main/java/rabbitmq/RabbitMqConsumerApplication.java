package rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 */
@SpringBootApplication
public class RabbitMqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqConsumerApplication.class,args);
    }
}
