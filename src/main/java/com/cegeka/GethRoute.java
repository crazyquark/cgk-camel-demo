package com.cegeka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component
public class GethRoute extends RouteBuilder {
    @Autowired
    private GethBean gethBean;
    
    @Override
    public void configure() throws Exception {
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
        
        from("jms:topic:eth")
            .bean(gethBean, "processMessage");
    }
}
