package org.seckill.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import org.seckill.dao.SecKillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liwc on 2016/9/26.
 */
//@Component:所有类型
@Service
public class SeckillServiceImpl implements SecKillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service依赖
    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // md5盐值字符串，用于混淆MD5
    private final String slat = "sadkfjalsdjfalksj23423^&*^&%&!EBJKH";

    public List<Seckill> getSecKillList() {
        return secKillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return secKillDao.queryById(seckillId);
    }

    /**
     * 秒杀地址暴露
     * 1.当数据库中查不到此商品信息,则该商品不参与秒杀
     *
     * @param seckillId
     * @return
     */
    public Exposer exportSeckillUrl(long seckillId) {
        /**
         * 如果没有秒杀产品记录,则代表产品不参与秒杀
         */

        Seckill secKill = secKillDao.queryById(seckillId);
        if (secKill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = secKill.getStartTime();
        Date endTime = secKill.getEndTime();
        Date nowTime = new Date();
        /**
         * 如果当前时间小于开始时间,则秒杀没有开始
         * 如果当前时间大于结束时间,则秒杀已经结束
         */
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        /**
         * 秒杀开启
         */
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * MD5的实现
     *
     * @param seckillId
     * @return
     */
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀
     * 1.md5比对失败或者MD5为空,则url被重写
     * 2.如果减库存失败,则秒杀关闭
     * 3.如果插入秒杀记录失败,则为重复秒杀
     *
     * @param seckillId
     * @param userPhone
     * @param md5       用于判断MD5是否变化
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑
        //减库存
        Date nowTime = new Date();
        try {
            int updateCount = secKillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.sucessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeat");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryBykIdWithSecKilled(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}
