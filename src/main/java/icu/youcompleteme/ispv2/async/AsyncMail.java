package icu.youcompleteme.ispv2.async;

import icu.youcompleteme.ispv2.config.Send2Mail;
import icu.youcompleteme.ispv2.entity.EmailMSG;
import icu.youcompleteme.ispv2.entity.IspUser;
import icu.youcompleteme.ispv2.util.StringConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * projectName: ispv2
 * className: AsyncMail
 * description: 类描述
 *
 * @author : chengjiang@asiainfo.com
 * @since : 2022/08/12
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncMail {

    private final Send2Mail send2Mail;

    private final String QQ_MAIL = "%s@qq.com";

    @Async
    public void successMail(IspUser ispUser) {
        // success
        EmailMSG emailMSG = new EmailMSG();
        emailMSG.setMailTo(String.format(QQ_MAIL, ispUser.getQq()));
        emailMSG.setRecieverName(ispUser.getQq());
        emailMSG.setTitle(String.format(StringConst.MESSAGE_TITLE, emailMSG.getRecieverName()));
        emailMSG.setText("<h1>尊敬的" + emailMSG.getRecieverName() + "用户</h1>您的地址："+ispUser.getArea() +
                "<hr>" +
                "<h2>注意事项：</h2>" +
                "<h5>1. 若需确保isp打卡是否成功，请自行登录isp查看</h5>" +
                "<h5>2. 注意自己的地址，严格按照（xx省/xx市/xx区，或者，xx省/xx市/xx县书写）</h5>" +
                "<h5>3. 定时打卡只服务长期地理位置不变的用户，如途中您的健康码变黄，请及时上报，否则，后果自负。</h5>" +
                "<hr>" +
                "该邮件为系统自动发送，请勿回复！！！");
        // send mail here
        send2Mail.sendmail(emailMSG);
    }

    @Async
    public void failMail(IspUser ispUser, String msg) {
        // failed
        EmailMSG emailMSG = new EmailMSG();
        emailMSG.setMailTo(String.format(QQ_MAIL, ispUser.getQq()));
        emailMSG.setRecieverName(ispUser.getQq());
        emailMSG.setTitle(String.format("ISP失败【自动发送】", emailMSG.getRecieverName()));
        emailMSG.setText("<h1>尊敬的" + emailMSG.getRecieverName() + "用户</h1>您的地址："+ispUser.getArea() +
                "<hr>" +
                "<h2>糟糕，isp打卡失败</h2>因为出现了非预期异常" +
                "<hr>" +
                msg.substring(0, msg.indexOf("(")) +
                "<hr>" +
                "该邮件为系统自动发送，请勿回复！！！");
        // send mail here
        send2Mail.sendmail(emailMSG);
    }
}
