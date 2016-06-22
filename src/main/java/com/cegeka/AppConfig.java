package com.cegeka;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
 
  @Autowired
  CamelContext camelContext;
 
//  @Bean
//  MyService myService() {
//    return new DefaultMyService(camelContext);
//  }
  
//  @Bean
//  CamelContextConfiguration contextConfiguration() {
//    return new CamelContextConfiguration() {
//      @Override
//      void beforeApplicationStart(CamelContext context) {
//        // your custom configuration goes here
//      }
//    };
//  }
 
}
