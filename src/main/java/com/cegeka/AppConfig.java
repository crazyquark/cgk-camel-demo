package com.cegeka;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.JdkIdGenerator;

/**
 * http://camel.apache.org/spring-boot.html
 * 
 * @author Cristian
 *
 */
@Configuration
public class AppConfig {

    @Autowired
    CamelContext camelContext;

    @Bean
    /**
     * Configures the Spring managed Camel context.
     * 
     * @return a Camel context configuration object
     */
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {

            @Override
            public void beforeApplicationStart(CamelContext context) {
                // A unique ID for the embedded broker
                JdkIdGenerator uuidGen = new JdkIdGenerator();
                String uuid = uuidGen.generateId().toString();

                // setup the ActiveMQ component
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                connectionFactory
                        .setBrokerURL("peer2://group-geth/broker-" + uuid + "?port=6060&persistent=false");
                connectionFactory.setTrustAllPackages(true);

                // and register it into the CamelContext
                JmsComponent answer = new JmsComponent();
                answer.setConnectionFactory(connectionFactory);

                camelContext.addComponent("jms", answer);
            }

            @Override
            public void afterApplicationStart(CamelContext context) {
                // TODO Auto-generated method stub

            }
        };
    }
}
