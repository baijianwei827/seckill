package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口
 * Created by liwc on 2016/9/26.
 */
public interface SecKillService {
    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSecKillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时,输出秒杀接口地址
     * 否则输出系统和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5       用于判断MD5是否变化
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;


    /**
     * 执行秒杀操作(使用存储过程)
     *
     * @param seckillId
     * @param userPhone
     * @param md5       用于判断MD5是否变化
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);


}
