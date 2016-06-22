package com.cegeka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
 
@Component
public class MyRoute extends RouteBuilder {
 
    @Override
    public void configure() throws Exception {
        from("direct:geth")
//        .transform(method("myBean", "saySomething"))
        .to("http4://localhost:8545")
        .to("stream:out");
    }
}
