package icu.youcompleteme.ispv2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.youcompleteme.ispv2.async.AsyncMail;
import icu.youcompleteme.ispv2.entity.IspUser;
import icu.youcompleteme.ispv2.enums.MessageEnum;
import icu.youcompleteme.ispv2.service.IspUserService;
import icu.youcompleteme.ispv2.util.BaseResponse;
import icu.youcompleteme.ispv2.util.ScriptForISPUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;


/**
 * projectName : isp
 * className: TimerController
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-02-06
 */
@RestController
@RequestMapping("/timer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimerController {

    private final IspUserService ispUserService;

    private final AsyncMail asyncMail;


    /**
     * @description : 凌晨更新第二天的状态
     * @param
     * @return : icu.youcompleteme.ispv2.util.BaseResponse
     * @author : git.jas0nch
     * date : 2022/8/16
     **/
    @Scheduled(cron = "0 0 0 * * ?")
    public BaseResponse cron() throws Exception {
        LambdaQueryWrapper<IspUser> wp = new LambdaQueryWrapper<>();
        wp.eq(IspUser::getIsSuccess, 1);
        List<IspUser> users = ispUserService.getBaseMapper().selectList(wp);
        users.forEach(e->{
            e.setIsSuccess(0);
        });
        if (users.size() > 0) {
            // 批量更新状态
            ispUserService.updateBatchById(users);
        }
        return BaseResponse.response(MessageEnum.SUCCESS);
    }


    @Scheduled(cron = "0 0 7/3 * * ?")
    public BaseResponse cronTask() throws Exception {
        LambdaQueryWrapper<IspUser> wp = new LambdaQueryWrapper<>();
        wp.eq(IspUser::getIsSuccess, 0);
        List<IspUser> users = ispUserService.getBaseMapper().selectList(wp);
        if (users.size() > 0) {
            thred(users);
        }
        return BaseResponse.response(MessageEnum.SUCCESS);
    }

    /**
     * @description : 定时打卡
     * @param
     * @return : void
     * @author : git.jas0nch
     * date : 2022/8/12
     **/
    @PostMapping("/add-user")
    public BaseResponse func(@RequestBody IspUser ispUser)
    {
        System.out.println(ispUser);
        LambdaQueryWrapper<IspUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IspUser::getUserId, ispUser.getUserId());
        if (Objects.nonNull(ispUserService.getBaseMapper().selectOne(wrapper))){
            ispUserService.getBaseMapper().updateById(ispUser);
        }else{
            ispUserService.save(ispUser);
        }
        return BaseResponse.response(MessageEnum.SUCCESS);
    }

    /**
     * @description : 一键打卡
     * @param
     * @return : void
     * @author : git.jas0nch
     * date : 2022/8/12
     **/
    @PostMapping("/one-time")
    public BaseResponse clock(@RequestBody IspUser ispUser)  {
        System.out.println(ispUser);
        ScriptForISPUtils script = new ScriptForISPUtils();
        try {
            script.scriptForISP(ispUser);
            // 异步打卡成功通知
            //
            ispUser.setIsSuccess(1);
            ispUserService.getBaseMapper().updateById(ispUser);
            asyncMail.successMail(ispUser);
            return BaseResponse.response(MessageEnum.SUCCESS);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.contains("成功")){
                // 异步打卡成功通知
                //
                ispUser.setIsSuccess(1);
                ispUserService.getBaseMapper().updateById(ispUser);
                asyncMail.successMail(ispUser);
            }else{
                asyncMail.failMail(ispUser, msg);
            }
            return BaseResponse.response(MessageEnum.ERROR);
        }
    }

    private void thred(List<IspUser> list) throws Exception {
        long start = System.currentTimeMillis();
        // 每1条数据开启一条线程
        int threadSize = 1;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<IspUser> cutList = null;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<IspUser> listChild = cutList;
            task = () -> {
                for (IspUser ispUser : listChild) {
                    ScriptForISPUtils script = new ScriptForISPUtils();
                    try {
                        script.scriptForISP(ispUser);
                        //
                        ispUser.setIsSuccess(1);
                        ispUserService.getBaseMapper().updateById(ispUser);
                        // 异步打卡成功通知
                        asyncMail.successMail(ispUser);
                    } catch (Exception e) {
                        String msg = e.getMessage();
                        if (msg.contains("成功")){
                            // 异步打卡成功通知
                            asyncMail.successMail(ispUser);
                            //
                            ispUser.setIsSuccess(1);
                            ispUserService.getBaseMapper().updateById(ispUser);
                        } else {
                            asyncMail.failMail(ispUser, msg);
                        }
                    }
                }
                return 1;
            };

            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<Integer>> results = exec.invokeAll(tasks);
        for (Future<Integer> future : results) {
            System.out.println(future.get());
        }
        // 关闭线程池
        exec.shutdown();
        System.out.println("线程任务执行结束");
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }


}
