package com.webhub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Webhub1Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Webhub1Application.class);

	public static void main(String[] args) {
		logger.info("Application started");
		
		SpringApplication.run(Webhub1Application.class, args);
	}
	
	
	 
	
	
	//it will enable CORS globally for frontend reuqests
	@Bean
	 public WebMvcConfigurer corsConfigurer() {
       return new WebMvcConfigurer() {
           @Override
           public void addCorsMappings(CorsRegistry registry) {
               registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST","PUT","GET","DELETE");
           }
       };
	}
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	

}
