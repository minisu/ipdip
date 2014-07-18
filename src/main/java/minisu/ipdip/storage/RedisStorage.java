package minisu.ipdip.storage;

import io.dropwizard.jackson.Jackson;
import minisu.ipdip.model.Decision;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class RedisStorage implements DecisionStorage {

    private final JedisPool jedisPool;
    private final ObjectMapper mapper = Jackson.newObjectMapper();

    public RedisStorage(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void store(Decision decision) throws IOException {
        Jedis jedis = jedisPool.getResource();
        jedis.set(decision.getId(), mapper.writeValueAsString(decision));
        jedisPool.returnResource(jedis);
    }

    @Override
    public Optional<Decision> get(String id) throws IOException{
        Jedis jedis = jedisPool.getResource();
        String decision = jedis.get(id);
        if(decision.equals("nil"))
            return Optional.absent();
        return Optional.of(mapper.readValue(decision, Decision.class));
    }
}
