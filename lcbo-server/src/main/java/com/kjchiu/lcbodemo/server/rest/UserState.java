package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.server.App;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserState {

    public static final String QUERY_HISTORY_PREFIX = App.REDIS_KEY_PREFIX + "history:";
    public static final int MAX_HISTORY_ITEMS = 10;

    private String user;
    private String token;
    private List<String> history;

    public UserState(String user, String token, List<String> history) {
        this.user = user;
        this.token = token;
        this.history = history;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public List<String> getHistory() {
        return history;
    }

    /**
     * Fetch user product query history
     * @param jedis
     * @param user
     * @return
     */
    public static List<String> fetchHistory(Jedis jedis, String user) {
        // why would you take something that guarantees order
        // and put it in a data structure that doesn't ensure order
        return jedis.zrevrangeWithScores(UserState.QUERY_HISTORY_PREFIX + user, 0, -1)
                .stream()
                .sorted(Comparator.comparingDouble(Tuple::getScore).reversed())
                .map(Tuple::getElement)
                .collect(Collectors.toList());

    }
}
