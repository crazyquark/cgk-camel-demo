package com.cegeka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CgkCamelDemoApplication {
    
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CgkCamelDemoApplication.class, args);
		
		GethBean gethBean = (GethBean)ctx.getBean(GethBean.class);
		
		if (args.length > 0 && args[2].equals("--send")) {
		    gethBean.sendMessage("Hey there buddy");
		}
	}
}
