package com.shodaqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Acer on 9/7/2017.
 */
@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.shodaqa","com.shodaqa.security"})
public class ApplicationRun {
	public static void main(String[] args){
		SpringApplication.run(ApplicationRun.class,args);
	}
}
