package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.entity.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/")
public class ProductResource extends LcboWrapper {

    public ProductResource(LcboClient client) {
        super(client);
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
    @Produces(MediaType.APPLICATION_JSON)
    public Paginated<Product> find(@QueryParam("q") String query) {
        return client.findProducts(query);
    }
}
