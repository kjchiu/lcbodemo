package com.kjchiu.lcbodemo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.LcboClientFactory;
import com.kjchiu.lcbodemo.server.rest.HelloWorld;
import com.kjchiu.lcbodemo.server.rest.ProductResource;
import com.kjchiu.lcbodemo.server.rest.UserResource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * runnable application configuration
 */
public class App extends ResourceConfig {

    public static final String REDIS_KEY_PREFIX = "lcbo:";

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String SERVER_PROPERTIES_PATH = "config/server.properties";
    private static final String DEFAULT_REDIS_HOST = "localhost";

    private int serverPort;
    private Server server;

    /**
     * Create app configuration
     * @throws ConfigurationException
     */
    public App() throws ConfigurationException {

        Configuration config;
        try {
            config = new PropertiesConfiguration(SERVER_PROPERTIES_PATH);
        } catch (ConfigurationException e) {
            logger.error("Failed to load server config", e);
            throw e;
        }

        String key = config.getString("api.key");
        serverPort = config.getInt("server.port");
        String salt = config.getString("salt");

        JedisPool jedisPool = new JedisPool();

        ObjectMapper mapper = new ObjectMapperProvider().getMapper();
        LcboClient client = LcboClientFactory.create(key, mapper);

        this.registerClasses(
                JacksonFeature.class,
                RolesAllowedDynamicFeature.class
        );

        // explicitly register resources
        Arrays.asList(
                new ProductResource(jedisPool, client),
                new UserResource(jedisPool, salt),
                new HelloWorld()

        ).forEach(this::register);

        this.register(new AuthFilter(jedisPool));

    }

    /**
     * Start accepting requests
     */
    public void run() {
        try {

            ServletContextHandler servletContextHandler = new ServletContextHandler();
            servletContextHandler.setContextPath("/");

            ServletContainer container = new ServletContainer(this);

            ServletHolder servlet = new ServletHolder(container);
            servletContextHandler.addServlet(servlet, "/*");

            server = new Server(serverPort);
            server.setHandler(servletContextHandler);
            logger.info("listening on port {}", serverPort);
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("server error", e);
        }

    }
}
