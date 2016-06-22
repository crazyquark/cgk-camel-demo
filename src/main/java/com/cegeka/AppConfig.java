package com.cegeka;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * http://camel.apache.org/spring-boot.html
 * 
 * @author chris
 *
 */
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
