package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by liwc on 2016/9/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void testSucessKilled() throws Exception {
        long id=1004L;
        long phone=18622372132L;
        int insertCount= successKilledDao.sucessKilled(id,phone);
        System.out.println(insertCount);
    }

    @Test
    public void testQueryBykIdWithSecKilled() throws Exception {
        long id=1004L;
        long phone=18622372132L;
        SuccessKilled successKilled= successKilledDao.queryBykIdWithSecKilled(id,phone);
        System.out.println(successKilled.getSeckill());
        System.out.println(successKilled);
    }
}