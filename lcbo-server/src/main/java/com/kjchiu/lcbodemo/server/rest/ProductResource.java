package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.entity.Product;
import com.kjchiu.lcbodemo.server.App;
import com.kjchiu.lcbodemo.server.AuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;
import java.util.Optional;

@Path("/")
public class ProductResource extends LcboWrapper {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

    public static final String QUERY_HISTORY_PREFIX = App.REDIS_KEY_PREFIX + "history:";
    public static final int MAX_HISTORY_ITEMS = 10;

    final JedisPool jedisPool;

    public ProductResource(JedisPool jedisPool, LcboClient client) {
        super(client);
        this.jedisPool = jedisPool;
    }

    @GET
    @Path("/product/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id")int id) {

        Optional<Product> maybeProduct = client.getProduct(id);
        return maybeProduct.map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/product")
    @PermitAll()
    @Produces(MediaType.APPLICATION_JSON)
    public Paginated<Product> find(
            @Context ContainerRequestContext context,
            @QueryParam("q") String query,
            @QueryParam("page") @DefaultValue("1") int page) {


        SecurityContext security = context.getSecurityContext();
        // really? embedded jetty won't do
        // async response? that doesn't seem right
        // for now this blocking call remains :(
        if (security.isUserInRole(AuthFilter.AUTHENTICATED_ROLE)) {
            try(Jedis jedis = jedisPool.getResource()) {
                String key = QUERY_HISTORY_PREFIX + security.getUserPrincipal().getName();
                long size = jedis.zadd(key, Instant.now().toEpochMilli(), query);
                if (size > MAX_HISTORY_ITEMS) {
                    jedis.zremrangeByRank(key, 0, 0);
                }
            }
        }

        return client.findProducts(query,  page);

    }
}
