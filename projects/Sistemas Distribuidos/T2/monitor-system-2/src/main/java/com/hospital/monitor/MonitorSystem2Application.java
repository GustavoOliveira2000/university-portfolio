package com.hospital.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorSystem2Application 
{

	public static void main(String[] args) 
        {
		SpringApplication.run(MonitorSystem2Application.class, args);
	}

}
