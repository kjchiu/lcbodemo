package com.kjchiu.lcbodemo.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.LcboClientFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String SERVER_PROPERTIES_PATH = "config/server.properties";

    public static void main(String[] args) throws IOException {




        logger.debug("hi");

        Configuration config;
        try {
            config = new PropertiesConfiguration(SERVER_PROPERTIES_PATH);
        } catch (ConfigurationException e) {
            logger.error("Failed to load server config", e);
            return;
        }

        String key = config.getString("api.key");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new ParanamerModule());

        LcboClient client = LcboClientFactory.create(key, mapper);
        client.getStore(1).ifPresent(store -> logger.info("store: {}", store));

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        Server server = new Server(8080);
        server.setHandler(context);

        ServletHolder restHolder = context.addServlet(ServletContainer.class, "/");
        restHolder.setInitParameter(
                "jersey.config.server.provider.packages",
                "com.kjchiu.lcbodemo.service"
        );

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
