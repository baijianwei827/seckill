package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by liwc on 2016/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecKillService secKillService;

    @Test
    public void testGetSecKillList() throws Exception {
        List<Seckill> list=secKillService.getSecKillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() throws Exception {
        Seckill seckill=secKillService.getById(1004);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillLogic() throws Exception {
        long id=1004;
        Exposer exposer=secKillService.exportSeckillUrl(1004);
        if(exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone=13502171128L;
            String md5=exposer.getMd5();
            try{
                SeckillExecution seckillExecution=secKillService.executeSeckill(id,phone,md5);
                logger.info("result={}",seckillExecution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch(SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else{
            logger.warn("exposer="+exposer);
        }

    }
}