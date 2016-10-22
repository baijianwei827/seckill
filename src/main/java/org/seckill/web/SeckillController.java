package org.seckill.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    /**
     * 查询
     * @param model
     * @return
     */
    @RequestMapping(value="/list/{page}",method = RequestMethod.GET)
    //使用@PathVariable注解page的页数
    public String list(@PathVariable(value = "page") Integer page,Model model){
        int rows=5;
        int totalPage=0;
        //分页
        int total= secKillService.getCount();
        int start=(page-1)*rows;
        int i=total%rows;
        if(i==0){
            totalPage=total/rows;
        }else{
            totalPage=(total/rows)+1;
        }
        List<Seckill> list=secKillService.getSecKillList(start,rows);
        //将查询出的list放入到PageInfo中
        PageInfo pageInfo =new PageInfo(list);
        model.addAttribute("page",page);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("total",total);
        model.addAttribute("list",list);
        return "list";
    }

    /**
     * 详情页
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value="/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = secKillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 暴露秒杀接口
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",method =RequestMethod.POST,
                    produces={"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exporer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = secKillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
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
            produces = {"application/json; charset=utf-8" })
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone", required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "手机号未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
//            SeckillExecution execution = secKillService.executeSeckill(seckillId, phone, md5);
            //通过存储过程调用
            SeckillExecution execution = secKillService.executeSeckillProcedure(seckillId, phone, md5);
            result = new SeckillResult<SeckillExecution>(true, execution);
            return result;
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult(true, execution);
        }
    }

    /**
     * 当前系统时间
     * @return
     */
    @RequestMapping(value="/time/now",method =RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }
}
