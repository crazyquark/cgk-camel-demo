package com.cegeka.activemq.transport.peer2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.activemq.broker.BrokerFactoryHandler;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportFactory;
import org.apache.activemq.transport.TransportServer;
import org.apache.activemq.transport.vm.VMTransportFactory;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IdGenerator;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;

public class Peer2TransportFactory extends TransportFactory {

    public static final ConcurrentMap<?, ?> BROKERS = new ConcurrentHashMap<Object, Object>();
    public static final ConcurrentMap<?, ?> CONNECTORS = new ConcurrentHashMap<Object, Object>();
    public static final ConcurrentMap<?, ?> SERVERS = new ConcurrentHashMap<Object, Object>();
    
    private static final IdGenerator ID_GENERATOR = new IdGenerator("peer-");

    @Override
    public Transport doConnect(URI location) throws Exception {
        VMTransportFactory vmTransportFactory = createTransportFactory(location);
        return vmTransportFactory.doConnect(location);
    }

    @Override
    public Transport doCompositeConnect(URI location) throws Exception {
        VMTransportFactory vmTransportFactory = createTransportFactory(location);
        return vmTransportFactory.doCompositeConnect(location);
    }

    /**
     * @param location
     * @return the converted URI
     * @throws URISyntaxException
     */
    private VMTransportFactory createTransportFactory(URI location) throws IOException {
        try {
            String group = location.getHost();
            String broker = URISupport.stripPrefix(location.getPath(), "/");

            if (group == null) {
                group = "default";
            }
            if (broker == null || broker.length() == 0) {
                broker = ID_GENERATOR.generateSanitizedId();
            }

            final Map<String, String> brokerOptions = new HashMap<String, String>(URISupport.parseParameters(location));
            if (!brokerOptions.containsKey("persistent")) {
                brokerOptions.put("persistent", "false");
            }
            
            final Integer port = brokerOptions.containsKey("port") ? Integer.parseInt(brokerOptions.get("port")) : 0;
            
            final URI finalLocation = new URI("vm://" + broker);
            final String finalBroker = broker;
            final String finalGroup = group;
            VMTransportFactory rc = new VMTransportFactory() {
                @Override
                public Transport doConnect(URI ignore) throws Exception {
                    return super.doConnect(finalLocation);
                };

                @Override
                public Transport doCompositeConnect(URI ignore) throws Exception {
                    return super.doCompositeConnect(finalLocation);
                };
            };
            rc.setBrokerFactoryHandler(new BrokerFactoryHandler() {
                @Override
                public BrokerService createBroker(URI brokerURI) throws Exception {
                    BrokerService service = new BrokerService();
                    IntrospectionSupport.setProperties(service, brokerOptions);
                    service.setBrokerName(finalBroker);
                    TransportConnector c = service.addConnector("tcp://0.0.0.0:" + port);
                    c.setDiscoveryUri(new URI("multicast://default?group=" + finalGroup));
                    service.addNetworkConnector("multicast://default?group=" + finalGroup);
                    return service;
                }
            });
            return rc;

        } catch (URISyntaxException e) {
            throw IOExceptionSupport.create(e);
        }
    }

    @Override
    public TransportServer doBind(URI location) throws IOException {
        throw new IOException("This protocol does not support being bound.");
    }

}
