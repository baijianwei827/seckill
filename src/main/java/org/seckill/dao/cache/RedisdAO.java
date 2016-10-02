package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 缓存的dao文件
 * Created by liwc on 2016/10/2.
 */
public class RedisDao {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    private JedisPool jedisPool;
    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }
    private RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //get--byte[]-->反序列化--Object(seckill)
                //采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                //如果不为空,则缓存中获取到了
                if (bytes != null) {
                    //通过schema获取新的对象
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }

            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    public String putSeckill(Seckill seckill){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                //set--byte[]-->序列化--Object(seckill)
                //采用自定义序列化
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

                int timeout=60*60;
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;

    }
}
