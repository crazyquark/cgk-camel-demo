package com.cegeka;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.springframework.stereotype.Component;
 
@Component
public class GethRoute extends RouteBuilder {
 
    @Override
    public void configure() throws Exception {
        from("direct:geth")
        .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .to("http4://localhost:8545")
        .to("stream:out");
    }
}
