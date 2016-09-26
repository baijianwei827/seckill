package org.seckill.dao;

import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和juint的整合,juint启动时加载ioc容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {
    //注入Dao依赖
    @Resource
    private  SecKillDao secKillDao;


    @org.junit.Test
    public void testQueryById() throws Exception {
        long id=1004;
        Seckill seckill=secKillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    /**
     * java没有保存形参的记录
     * @throws Exception
     */
    @org.junit.Test
    public void testQueryAll() throws Exception {
        List<Seckill> list=secKillDao.queryAll(0,100);
        for(Seckill seckill:list){
            System.out.println(seckill);
        }
    }
    @org.junit.Test
    public void testReduceNumber() throws Exception {
        Date killTime=new Date();
        int updateCount=secKillDao.reduceNumber(1004L,killTime);
        System.out.println(updateCount);
    }
}