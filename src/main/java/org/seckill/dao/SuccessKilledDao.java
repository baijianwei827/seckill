package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by liwc on 2016/9/22.
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细,可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int sucessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询记录并携带秒杀商品实体
     * @param secKillId
     * @return
     */
    SuccessKilled queryBykIdWithSecKilled(@Param("seckillId")long secKillId,@Param("userPhone")long userPhone);


}
