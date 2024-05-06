package config;

import com.example.easynotes.exception.Messages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ComponentScan
@Configuration
public class AppConfig {
    private String messages;
    private HttpStatus status;
    @Bean
    public Messages messages(){
        return new Messages();
    }

}
