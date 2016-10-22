package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liwc on 2016/9/22.
 */
public interface SecKillDao {
    /**
     * 减库存
     *
     * @param seckillId
     * @param killTime
     * @return 如果影响>=1,表示更新行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询库存
     *
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 分页,需要在参数前增加注解:@Param,用于告诉mybaits所传的参数
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     *
     * @return
     */
    int getCount();

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);


}
