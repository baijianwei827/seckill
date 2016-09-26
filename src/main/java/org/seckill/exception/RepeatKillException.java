package org.seckill.exception;

/**
 * 重复秒杀异常
 * Created by liwc on 2016/9/26.
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
