package org.seckill.exception;

/**
 * 秒杀相关义务异常
 * Created by liwc on 2016/9/26.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
