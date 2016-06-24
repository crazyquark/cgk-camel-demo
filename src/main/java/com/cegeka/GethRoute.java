package com.cegeka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.JdkIdGenerator;
 
@Component
public class GethRoute extends RouteBuilder {
    @Autowired
    private GethBean gethBean;
    
    private String UUID;
    
    @Override
    public void configure() throws Exception {
        JdkIdGenerator generator = new JdkIdGenerator();
        this.UUID = generator.generateId().toString();
        
        from("direct:geth")
            .to("stream:out");
        
//        from("jms:queue:inbox")
//            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
//            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
////            .multicast()
//            .to("http4://localhost:8545"/*, "jms:queue:inbox"*/) 
//            .to("jms:queue:process");
////            .bean(gethBean, "processPeers");
//        
//        from("jms:queue:process")
//            .bean(gethBean, "processPeers");
//        
////        from("jms:queue:inbox")
////            .bean(gethBean, "processMessage");
        
        from("jms:topic:eth?exchangePattern=InOnly")
            .filter(header("uuid").isNotEqualTo(this.UUID.toString()))
            .bean(gethBean, "processMessage");
        
        from("timer:sender?period=2000")
            .bean(gethBean, "sendMessage");
    }
    
    public String getUUID() {
        return this.UUID;
    }
}
