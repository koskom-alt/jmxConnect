package com.koskom.utils;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;

import javax.jms.InvalidSelectorException;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JmxConnector {


    public void getConnectionFactory() throws JMException, IOException, InvalidSelectorException {


        JMXServiceURL url = new JMXServiceURL ("service:jmx:rmi:///jndi/rmi://localhost:1616/jmxrmi");
        Map<String, Object> envVars = new HashMap<String, Object> ();

        envVars.put(JMXConnector.CREDENTIALS, new String[] {"admin", "activemq" });
        JMXConnector jmxc = JMXConnectorFactory.newJMXConnector (url, envVars);
        jmxc.connect ();

        MBeanServerConnection conn = jmxc.getMBeanServerConnection();

        ObjectName activeMQ = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost");

        BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(conn, activeMQ, BrokerViewMBean.class, true);
        for (ObjectName name : mbean.getQueues()) {
            System.out.println (name.getKeyProperty ("destinationName"));
            /*QueueViewMBean queueMbean = (QueueViewMBean)
                    MBeanServerInvocationHandler.newProxyInstance(conn, name, QueueViewMBean.class, true);

                System.out.println (queueMbean.getName ());*/
        }
    }

    public static void main (String[] args) throws IOException, JMException, InvalidSelectorException {
        JmxConnector jmxConnector = new JmxConnector ();
        jmxConnector.getConnectionFactory ();
    }
}
