package icu.youcompleteme.ispv2.config;

import icu.youcompleteme.ispv2.entity.EmailMSG;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * projectName: sendemail
 * className: Send2Mail
 * description: 类描述
 *
 * @author : chengjiang@asiainfo.com
 * @since : 2022/07/27
 */

//@ConfigurationProperties

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Send2Mail {

    @Value("${my.qq}")
    private String mailFrom;

    private final JavaMailSender javaMailSender;


    public void sendmail(EmailMSG emailMSG){

        MimeMessage message = javaMailSender.createMimeMessage();
        // check
        this.checkEmailMSG(emailMSG);
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailFrom);
            // to
            helper.setTo(emailMSG.getMailTo().split(","));
            //
            helper.setSubject(emailMSG.getTitle());
            // html: true
            helper.setText(emailMSG.getText(), true);
            // send file?
            if (emailMSG.isHasFile() && StringUtils.isNotEmpty(emailMSG.getFilePath())){
                String filePath = emailMSG.getFilePath();
                FileSystemResource file = new FileSystemResource(new File(emailMSG.getFilePath()));
                String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                helper.addAttachment(fileName, file);
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    private void checkEmailMSG(EmailMSG emailMSG){
        Assert.notNull(emailMSG, "邮件请求不能为空");
        Assert.notNull(emailMSG.getTitle(), "邮件标题不能为空");
        Assert.notNull(emailMSG.getMailTo(), "邮件发送人不能为空");
        Assert.notNull(emailMSG.getText(), "邮件请求内容不能为空");
    }
}
