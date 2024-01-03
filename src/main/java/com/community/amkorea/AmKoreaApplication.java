package com.community.amkorea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AmKoreaApplication {

  public static void main(String[] args) {
    SpringApplication.run(AmKoreaApplication.class, args);
  }

}
