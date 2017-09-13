package com.kjchiu.lcbodemo.server;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    public static final String AUTHENTICATED_ROLE = "AUTHENTICATED";
    public static final int SESSION_LENGTH_SECONDS = 3600; // 1 hour
    public static final String SESSION_TOKEN_PREFIX = App.REDIS_KEY_PREFIX + "session:";

    JedisPool jedisPool;

    public AuthFilter(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authHeader = requestContext.getHeaderString("Auth-Token");
        try(Jedis jedis = jedisPool.getResource()) {

            final String user = jedis.get(SESSION_TOKEN_PREFIX + authHeader);
            final boolean isAuthed = ! StringUtils.isBlank(user);

            if (isAuthed) {
                jedis.expire(authHeader,  SESSION_LENGTH_SECONDS);
            }

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> Optional.ofNullable(user).orElse("");
                }

                @Override
                public boolean isUserInRole(String s) {
                    return isAuthed;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Auth-Token";
                }
            });
        }
    }
}
