package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by liwc on 2016/9/27.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecKillService secKillService;

    @RequestMapping(name="/list",method = RequestMethod.GET)
    public String list(Model model){
        List<Seckill> list=secKillService.getSecKillList();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value="/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId")Long seckillId, Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=secKillService.getById(seckillId);
        if(seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    /**
     * 暴露秒杀结果
     * ajax返回json
     */
    @RequestMapping(value = "/{seckillId}/exposer",method =RequestMethod.POST,
                    produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exporer(Long seckillId){
        SeckillResult<Exposer> result;
        try{
         Exposer exposer=secKillService.exportSeckillUrl(seckillId);
         result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return  result;
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @RequestMapping(value="/{seckillId}/{md5}/execution",method =RequestMethod.POST,
            produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "phone",required = false) Long phone){
        if(phone==null){
            return new SeckillResult<SeckillExecution>(false,"手机号未注册");
        }
        SeckillResult<SeckillExecution>result;
        try{
            SeckillExecution execution=secKillService.executeSeckill(seckillId,phone,md5);
            result=new SeckillResult<SeckillExecution>(true,execution);
            return result;
        }catch(RepeatKillException e){
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new  SeckillResult(false,execution);
        } catch (SeckillCloseException e){
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new  SeckillResult(false,execution);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new  SeckillResult(false,execution);
        }
    }

    /**
     * 当前系统时间
     * @return
     */
    @RequestMapping(value="/time/now",method =RequestMethod.GET)
    public SeckillResult<Long>  time(){
        Date now=new Date();
        return new SeckillResult(true,now.getTime());
    }
}
