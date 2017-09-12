package com.kjchiu.lcbodemo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.LcboClientFactory;
import com.kjchiu.lcbodemo.server.rest.LcboWrapper;
import com.kjchiu.lcbodemo.server.rest.ProductResource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String SERVER_PROPERTIES_PATH = "config/server.properties";

    private static final List<Function<LcboClient, LcboWrapper>> resourceFactories = Arrays.asList(
            ProductResource::new
    );

    public static void main(String[] args) throws IOException, URISyntaxException {

        logger.debug("hi");

        Configuration config;
        try {
            config = new PropertiesConfiguration(SERVER_PROPERTIES_PATH);
        } catch (ConfigurationException e) {
            logger.error("Failed to load server config", e);
            return;
        }

        String key = config.getString("api.key");
        int port = config.getInt("server.port");

        ObjectMapper mapper = new ObjectMapperProvider().getMapper();

        LcboClient client = LcboClientFactory.create(key, mapper);
//        client.getStore(1).ifPresent(store -> logger.info("store: {}", store));

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.setBaseResource(Resource.newClassPathResource("static-root"));

        logger.info("listening on port {}", port);
        Server server = new Server(port);
        server.setHandler(servletContextHandler);

        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(JacksonFeature.class);
        resourceFactories.stream().map(ctor -> ctor.apply(client)).forEach(resourceConfig::register);
        ServletContainer container = new ServletContainer(resourceConfig);

        ServletHolder servlet = new ServletHolder(container);
        servletContextHandler.addServlet(servlet, "/*");
        run(server);

    }

    private static void run(Server server) {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("server error", e);
        }

    }
}
