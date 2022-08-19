package icu.youcompleteme.ispv2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.youcompleteme.ispv2.async.AsyncMail;
import icu.youcompleteme.ispv2.config.Send2Mail;
import icu.youcompleteme.ispv2.entity.EmailMSG;
import icu.youcompleteme.ispv2.entity.IspUser;
import icu.youcompleteme.ispv2.service.IspUserService;
import icu.youcompleteme.ispv2.util.ScriptForISPUtils;
import icu.youcompleteme.ispv2.util.StringConst;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
class Ispv2ApplicationTests {

    @Autowired
    private IspUserService ispUserService;
//
    @Autowired
    private Send2Mail send2Mail;

    @Autowired
    private AsyncMail asyncMail;

    private final String QQ_MAIL = "%s@qq.com";
//
    @Test
    void contextLoads() {
        LambdaQueryWrapper<IspUser> wp = new LambdaQueryWrapper<>();
        wp.eq(IspUser::getIsSuccess, 1);
        List<IspUser> users = ispUserService.getBaseMapper().selectList(wp);
        System.out.println(users);
        users.forEach(e->{
            e.setIsSuccess(0);
        });
        System.out.println(users);
        if (users.size() > 0) {
            // 批量更新状态
            ispUserService.updateBatchById(users);
        }

    }
//
    @Test
    void func() {
        List<IspUser> users = ispUserService.queryAll();
        if (users.size() > 0) {
            for (int i = 0; i < 3; i++) {
                ScriptForISPUtils script = new ScriptForISPUtils();
                try {
                    script.scriptForISP(users.get(0));
                    // success
                    EmailMSG emailMSG = new EmailMSG();
                    emailMSG.setMailTo(String.format(QQ_MAIL, users.get(0).getQq()));
                    emailMSG.setRecieverName(users.get(0).getQq());
                    emailMSG.setTitle(String.format(StringConst.MESSAGE_TITLE, emailMSG.getRecieverName()));
                    emailMSG.setText("<h1>尊敬的" + emailMSG.getRecieverName() + "用户</h1>" +
                            "<hr>" +
                            "<h2>注意事项：</h2>" +
                            "<h5>1. 若需确保isp打卡是否成功，请自行登录isp查看</h5>" +
                            "<h5>2. 注意自己的地址，严格按照（xx省/xx市/xx区，或者，xx省/xx市/xx县书写）</h5>" +
                            "<h5>3. 定时打卡只服务长期地理位置不变的用户，如途中您的健康码变黄，请及时上报，否则，后果自负。</h5>" +
                            "<hr>" +
                            "该邮件为系统自动发送，请勿回复！！！");
                    // send mail here
                    send2Mail.sendmail(emailMSG);
                } catch (Exception e) {
                    // failed
                    EmailMSG emailMSG = new EmailMSG();
                    emailMSG.setMailTo(String.format(QQ_MAIL, users.get(0).getQq()));
                    emailMSG.setRecieverName(users.get(0).getQq());
                    emailMSG.setTitle(String.format("ISP失败【自动发送】%s", emailMSG.getRecieverName()));
                    emailMSG.setText("<h1>尊敬的" + emailMSG.getRecieverName() + "用户</h1>" +
                            "<hr>" +
                            "<h2>糟糕，isp打卡失败</h2>因为出现了非预期异常" +
                            "<hr>" +
                            "该邮件为系统自动发送，请勿回复！！！");
                    // send mail here
                    send2Mail.sendmail(emailMSG);
                    e.printStackTrace();
                }
            }
        }
    }
//
    @Test
    void thread() throws Exception {
        LambdaQueryWrapper<IspUser> wp = new LambdaQueryWrapper<>();
        wp.eq(IspUser::getIsSuccess, 0);
        List<IspUser> users = ispUserService.getBaseMapper().selectList(wp);
        if (users.size() > 0) {
            thred(users);
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
