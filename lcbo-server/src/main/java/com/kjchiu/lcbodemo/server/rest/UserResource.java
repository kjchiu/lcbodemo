package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.server.App;
import com.kjchiu.lcbodemo.server.AuthFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.security.RolesAllowed;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private static final Charset charset = Charset.forName("UTF-8");
    private static final String LOGIN_KEY_PREFIX = App.REDIS_KEY_PREFIX + "pass:";

    private final int ITERATIONS = 1000;
    private final JedisPool jedisPool;
    private final String salt;

    private static class BasicCredentials {

        private String user;
        private String password;


        public BasicCredentials(ContainerRequestContext context) {
            String auth = context.getHeaderString("Authorization");

            if (!auth.startsWith("Basic ")) {
                return;
            }
            String encoded = auth.substring("Basic ".length());
            String decoded = new String(Base64.getDecoder().decode(encoded), charset);
            String[] tokenized = decoded.split(":");
            if (tokenized.length != 2) {
                return;
            }

            this.user = tokenized[0];
            this.password = tokenized[1];
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }
    }

    public UserResource(JedisPool jedisPool, String salt) {
        this.jedisPool = jedisPool;
        this.salt = salt;
    }

    @PUT
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@Context ContainerRequestContext context) {
        BasicCredentials credentials = new BasicCredentials(context);

        try (Jedis jedis = jedisPool.getResource()) {
            String passwordHash = hashPassword(
                    credentials.getPassword());
            String key = LOGIN_KEY_PREFIX + passwordHash;
            long rc = jedis.setnx(key, credentials.getUser());
            if (rc == 0) {
                return "";
            }

            String token = generateAuthToken(jedis, credentials.getUser());
            return token;


        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to create user", e);
        }
        return "";

    }

    @POST
    @Path("/user")
    @RolesAllowed(AuthFilter.AUTHENTICATED_ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    public UserState auth(@Context ContainerRequestContext context) {
        String user = context.getSecurityContext().getUserPrincipal().getName();
        try(Jedis jedis =  jedisPool.getResource()) {
            String token = context.getHeaderString(AuthFilter.HEADER_AUTH_TOKEN);
            return  getUserState(user, token, jedis);
        }
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public UserState login(@Context ContainerRequestContext context) {
        BasicCredentials credentials = new BasicCredentials(context);

        try {

            String passwordHash = hashPassword(credentials.getPassword());

            try (Jedis jedis = jedisPool.getResource()) {
                String actualUser = jedis.get(LOGIN_KEY_PREFIX + passwordHash);
                if (!StringUtils.equals(credentials.getUser(), actualUser)) {
                    return null;
                }

                String token = generateAuthToken(jedis, credentials.getUser());
                return getUserState(credentials.getUser(), token, jedis);

            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to generate password hash", e);
            return null;
        }

    }

    /**
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(charset), ITERATIONS, 128);
        SecretKeyFactory factory;
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] bytes = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Generate/save auth token
     *
     * @param jedis
     * @return auth token
     */
    private String generateAuthToken(Jedis jedis, String user) {
        SecureRandom rng = new SecureRandom();
        byte[] tokenBytes = new byte[24];
        rng.nextBytes(tokenBytes);
        String token = Base64.getEncoder().encodeToString(tokenBytes);
        jedis.setnx(AuthFilter.SESSION_TOKEN_PREFIX + token, user);
        jedis.expire(App.REDIS_KEY_PREFIX + token, AuthFilter.SESSION_LENGTH_SECONDS);
        return token;
    }

    /**
     * Construct client view of user
     *
     * @param user  user name
     * @param token current session token
     * @param jedis
     * @return
     */
    public UserState getUserState(String user, String token, Jedis jedis) {
        return new UserState(user, token, UserState.fetchHistory(jedis, user));

    }
}
