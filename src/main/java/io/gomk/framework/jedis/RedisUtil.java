package io.gomk.framework.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;


/**
 * @Auther: ZHG
 * @Date: 2019-03-29 14:46
 * @Description: Redis工具类
 */

//@Component
public final class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private JedisCluster jedisCluster;

    private static JedisCluster jCluster;

    @PostConstruct
    public void init() {
        jCluster = jedisCluster;
    }


/**
     * @Description: 存储字符串
     * @param: key
     * @param: value
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static String putString(String key, String value) {
        jCluster.del(key);
        return jCluster.set(key, value);
    }


/**
     * @Description: 存储字符串
     * @param: key
     * @param: value
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static String putString(byte[] key, byte[] value) {
        jCluster.del(key);
        return jCluster.set(key, value);
    }


/**
     * @Description: 获取字符串
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static String getString(String key) {

        return jCluster.get(key);
    }


/**
     * @Description: 获取字符串
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static byte[] getString(byte[] key) {

        return jCluster.get(key);
    }


/**
     * @Description: 存放list
     * @param: key
     * @param: list
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Long putList(String key, String[] list) {

        jCluster.del(key);
        return jCluster.lpush(key, list);
    }

    public static Long putList(byte[] key, byte[][] list) {
        jCluster.del(key);
        return jCluster.lpush(key, list);
    }


/**
     * @Description: 获取list
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static String getList(String key) {

        return jCluster.rpop(key);
    }

    public static byte[] getList(byte[] key) {

        return jCluster.rpop(key);
    }


/**
     * @Description: 存放hash
     * @param: key
     * @param: hash
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static String putHash(String key, Map<String, String> valueMap) {
        jCluster.hdel(key,mapKeyToArr(valueMap));
        return jCluster.hmset(key, valueMap);
    }
//    获得map中的所有的key用来删除
    public static String[] mapKeyToArr(Map<String, String> hash) {
        Set<String> keySets = hash.keySet();
        String[] keys = new String[keySets.size()];
        int i = 0;
        for (String keySet : keySets) {
            keys[i] = keySet;
            i++;
        }
        return keys;
    }
    public static String putHash(byte[] key, Map<byte[], byte[]> hash) {

        jCluster.del(key);
        return jCluster.hmset(key, hash);
    }


/**
     * @Description: 获取hash
     * @param: key
     * @param: list
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static List<String> getHash(String key, String[] list) {

        return jCluster.hmget(key, list);
    }

    public static List<byte[]> getHash(byte[] key, byte[][] list) {

        return jCluster.hmget(key, list);
    }


/**
     * @Description: 获取hash全部
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Map<String, String> getHashAll(String key) {
//        System.out.println(jCluster.ttl(key));
        return jCluster.hgetAll(key);
    }

    public static Map<byte[], byte[]> getHashAll(byte[] key) {

        return jCluster.hgetAll(key);
    }

/**
     * @Description: 存储set
     * @param: key
     * @param: set
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Long putSet(String key, String[] set) {

        jCluster.del(key);
        return jCluster.sadd(key, set);
    }

    public static Long putSet(byte[] key, byte[][] set) {

        jCluster.del(key);
        return jCluster.sadd(key, set);
    }


/**
     * @Description: 获取set
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Set<String> getSet(String key) {

        return jCluster.smembers(key);
    }

    public static Set<byte[]> getSet(byte[] key) {
        return jCluster.smembers(key);
    }


/**
     * @Description: 存储有序集合
     * @param: key
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Long putStortedSet(String key, Map<String, Double> scoreMembers) {

        jCluster.del(key);
        return jCluster.zadd(key, scoreMembers);
    }

    public static Long putStortedSet(byte[] key, Map<byte[], Double> scoreMembers) {

        jCluster.del(key);
        return jCluster.zadd(key, scoreMembers);
    }



/**
     * @Description: 获取有序集合根据升序
     * @param: key
     * @param: start
     * @param: end
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Set<Tuple> getStortedSetByAsc(String key, Long start, Long end) {

        return jCluster.zrangeWithScores(key, start, end);
    }

    public static Set<Tuple> getStortedSetByAsc(byte[] key, Long start, Long end) {

        return jCluster.zrangeWithScores(key, start, end);
    }


/**
     * @Description: 获取有序集合根据降序
     * @param: key
     * @param: start
     * @param: end
     * @return:
     * @auther: ZHG
     * @date: 2019/1/8 0008 16:38
     */

    public static Set<Tuple> getStortedSetByDesc(String key, Long start, Long end) {

        return jCluster.zrevrangeWithScores(key, start, end);
    }

    public static Set<Tuple> getStortedSetByDesc(byte[] key, Long start, Long end) {

        return jCluster.zrevrangeWithScores(key, start, end);
    }

    public static Set<String> getKeysSet(String regularKeys) {
//        jCluster.
        return jCluster.hkeys(regularKeys);
    }
    public static Long delKeys(String regularKeys) {

        return jCluster.del(regularKeys);
    }
//  redis中key的存活时间设置，不设置默认永久存活
    public static long keyLiveTimeSet(String regularKeys,int liveTime) {
        return jCluster.expire(regularKeys,liveTime);
    }
//    查询key在redis的状态 -1 永久 -2不存在  正数为存活秒数
    public static long getKeyLiveTime(String regularKeys) {
        return jCluster.ttl(regularKeys);
    }
}
