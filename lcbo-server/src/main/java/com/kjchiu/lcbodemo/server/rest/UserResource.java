package com.kjchiu.lcbodemo.server.rest;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.kjchiu.lcbodemo.server.App;
import com.kjchiu.lcbodemo.server.AuthFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Path("/")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private static final Charset charset = Charset.forName("UTF-8");

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
            String base64UserPassword = auth.substring("Basic ".length());
            String userPassword = new String(Base64.getDecoder().decode(base64UserPassword), charset);
            String[] tokenized = userPassword.split(":");
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
    public String create(@Context ContainerRequestContext context) {
        BasicCredentials credentials = new BasicCredentials(context);

        try (Jedis jedis = jedisPool.getResource()) {
            String key = App.REDIS_KEY_PREFIX + "user:" + credentials.getUser();
            String passwordHash = hashPassword(
                    credentials.getPassword());
            long rc = jedis.setnx(key, passwordHash);
            if (rc == 0) {
                return "";
            }

            return generateAuthToken(jedis, credentials.getUser());


        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to create user", e);
        }
        return "";

    }

    @GET
    @Path("/user")
    public String auth(@Context ContainerRequestContext context) {
        BasicCredentials credentials = new BasicCredentials(context);

        try {

            String passwordHash = hashPassword(credentials.getPassword());

            try (Jedis jedis = jedisPool.getResource()) {
                String actualUser = jedis.get(App.REDIS_KEY_PREFIX + passwordHash);
                if (! StringUtils.equals(credentials.getUser(), actualUser)) {
                    return "";
                }

                return generateAuthToken(jedis, credentials.getUser());

            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to generate password hash", e);
            return "";
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
}
