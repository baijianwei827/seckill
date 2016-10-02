package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SecKillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by liwc on 2016/10/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private  long id=1005;
    @Autowired
    private  RedisDao redisDao;
    @Autowired
    private SecKillDao seckilldao;
    @Test
    public void testSeckill() throws Exception {
        Seckill seckill=redisDao.getSeckill(id);
        if(seckill==null){
            seckill=seckilldao.queryById(id);
            if(seckill!=null){
                String result=redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill=redisDao.getSeckill(id);
                System.out.println(seckill);
            }

        }
    }
}