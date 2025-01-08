package itm.microservices.task4;

import itm.microservices.task4.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Task4Application implements CommandLineRunner{
    private final UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(Task4Application.class, args);
    }

        public Task4Application(UserService userService) {
            this.userService = userService;
        }

        @Override
        public void run(String... args) throws Exception {
            userService.execute();
        }

}