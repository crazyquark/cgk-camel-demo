/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cegeka;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Component
public class GethBean {
    @Autowired
    CamelContext camelContext;
    
    @Autowired
    GethRoute gethRoute;
    
    private boolean enableSender = false;
    
    private static final String ETH_PEERS_JSON = "{\"jsonrpc\":\"2.0\",\"method\":\"net_peerCount\",\"params\":[],\"id\":74}";
    
    public void enableSender() {
        this.enableSender = true;
    }
    
    public void getPeers() {
        ProducerTemplate template = camelContext.createProducerTemplate();

        template.sendBody("jms:queue:inbox", ETH_PEERS_JSON);
    }
    
    public void processPeers(String input) {
        Gson gson = new Gson();
        GethResponse response = gson.fromJson(input, GethResponse.class);
        
        System.out.println(response);
    }
    
    public void sendMessage() throws Exception { 
        if (!enableSender) {
            return;
        }
        
        Date now = new Date();
        System.out.println("[" + now.toString() + "]Sending to all!");
        
        ProducerTemplate template = camelContext.createProducerTemplate();
        
        template.sendBodyAndHeader("jms:topic:eth", ExchangePattern.InOnly , new Message("Hey there, little buddy!"), "uuid", gethRoute.getUUID());
        
        template.stop();
    }
    
    public void createPdf(Object receiver, String methodName) throws Exception {
    	ProducerTemplate template = camelContext.createProducerTemplate();
    		    
    	template.send("direct:pdf", new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody("This is a PDF!\n");
				
				exchange.setProperty("receiver", receiver);
				exchange.setProperty("method", methodName);
			}
		});
    	
    	template.stop();
    }
    
    public void processPdf(OutputStream pdf, Exchange exchange) {
    	Object receiver = exchange.getProperty("receiver");
    	String methodName = (String) exchange.getProperty("method");
    	
    	try {
			Method method = receiver.getClass().getMethod(methodName, OutputStream.class);
			
			method.invoke(receiver, pdf);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void printPdf(OutputStream pdf){
    	System.out.println(pdf);
    }
    
    public void processMessage(Message message, Exchange exchange) {
        Date now = new Date();
        
        System.out.print("[" + now.toString() + "]Someone sent me this: " + message + "\n");
    }
}
