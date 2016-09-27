package org.seckill.dto;

/**
 * Created by liwc on 2016/9/27.
 * ajax发送封装json结果
 */
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;//错误原因

    public SeckillResult(boolean success,T data) {
        this.success = success;
        this.data=data;
    }

    public SeckillResult( boolean success,String error) {
        this.error = error;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
