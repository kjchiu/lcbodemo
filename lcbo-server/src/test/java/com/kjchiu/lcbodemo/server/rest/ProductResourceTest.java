package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.LcboClient;
import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.entity.Product;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

import static org.mockito.Mockito.*;

public class ProductResourceTest {

    private LcboClient client;
    private ProductResource resource;
    private JedisPool jedisPool;
    private Jedis jedis;

    @Before
    public void beforeEach() {
        client = mock(LcboClient.class);
        jedisPool = mock(JedisPool.class);
        jedis = mock(Jedis.class);
        resource = spy(new ProductResource(jedisPool, client));

    }

    @Test
    public void find_NotAuthenticated_ElideHistory() {
        ContainerRequestContext context = mock(ContainerRequestContext.class);
        SecurityContext security = mock(SecurityContext.class);

        when(context.getSecurityContext()).thenReturn(security);
        when(client.findProducts("some query", 1)).thenReturn(mock(Paginated.class));

        resource.find(context, "some query", 1);
        verify(jedisPool, never()).getResource();
        verify(client).findProducts("some query", 1);
        verify(resource, never()).storeQueryHistory(any(), any());
    }

    @Test
    public void find_Authenticated_StoreHistory() {
        ContainerRequestContext context = mock(ContainerRequestContext.class);
        SecurityContext security = mock(SecurityContext.class);

        when(context.getSecurityContext()).thenReturn(security);
        when(security.isUserInRole(any())).thenReturn(true);
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("name");
        when(security.getUserPrincipal()).thenReturn(principal);

        when(client.findProducts("some query", 1)).thenReturn(mock(Paginated.class));
        when(jedisPool.getResource()).thenReturn(jedis);

        resource.find(context, "some query", 1);
        verify(client).findProducts("some query", 1);
        verify(resource).storeQueryHistory(any(), any());
    }
}
