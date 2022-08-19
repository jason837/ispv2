package icu.youcompleteme.ispv2.util;

import lombok.SneakyThrows;
import org.openqa.selenium.UnhandledAlertException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * projectName : isp
 * className: TimerUtils
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-02-06
 */
@Component
public class TimerUtils {
    @SneakyThrows
    public synchronized void func(){
        //计算一天的毫秒数
        long dayS = 24 * 60 * 60 * 1000;
        // 每天的08:00:00执行任务
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '08:00:00'");
        // 首次运行时间
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
        // 如果已过当天设置时间，修改首次运行时间为明天
        if(System.currentTimeMillis() > startTime.getTime()){
            startTime = new Date(startTime.getTime() + dayS);
        }

        Timer t = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                // 定时打卡
                ScriptForISPUtils scriptForISP = new ScriptForISPUtils();
                try {
                    scriptForISP.scriptForISP();
                }
                catch (UnhandledAlertException h){
                    System.out.println("您已经登录过了哦~~~");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 以每24小时执行一次
        t.scheduleAtFixedRate(task, startTime, dayS);

    }
}
