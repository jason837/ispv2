package icu.youcompleteme.ispv2.config;

import icu.youcompleteme.ispv2.util.ScriptForISPUtils;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.UnhandledAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * projectName : isp
 * className: TaskJobImpl
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-02-06
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskJob {


    /**
     * @description : 定时任务，第一版，每天早上8.点，
     *                          第二版，改为12点。
     * @param
     * @return : void
     * @author : git.jas0nch
     * date : 2022/2/6
     **/
    @Scheduled(cron = "0 0 8 * * ?")
    public void job1() {
        ScriptForISPUtils scriptForISP = new ScriptForISPUtils();
        try {
            scriptForISP.scriptForISP();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(fixedDelay = 2000L)
//    public void job2() {
//        System.out.println("通过fixedDelay定义的定时任务");
//    }
//
//    @Scheduled(fixedRate = 5000L)
//    public void job3() {
//        System.out.println("通过fixedRate定义的定时任务");
//    }
}

