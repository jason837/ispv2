package icu.youcompleteme.ispv2.entity;

import lombok.Data;

/**
 * projectName: sendemail
 * className: emailmsg
 * description: 类描述
 *
 * @author : chengjiang@asiainfo.com
 * @since : 2022/07/27
 */

@Data
public class EmailMSG {

    private String mailTo;

    private String recieverName;

    private String title;

    private String text;

    private String filePath;

    private boolean hasFile = false;

}
