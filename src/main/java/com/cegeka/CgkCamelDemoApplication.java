package com.cegeka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CgkCamelDemoApplication {
    
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CgkCamelDemoApplication.class, args);
		
		GethBean bean = (GethBean)ctx.getBean(GethBean.class);
		
		bean.getPeers();
	}
}
