package com.casestudy.BetCaseStudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRetry
@EnableScheduling
public class BetCaseStudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BetCaseStudyApplication.class, args);
	}
}
