package com.casestudy.BetCaseStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BetCaseStudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BetCaseStudyApplication.class, args);
	}
}
