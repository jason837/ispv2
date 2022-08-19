package icu.youcompleteme.ispv2.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 登录用户(IspUser)表实体类
 *
 * @author chengjiang
 * @since 2022-08-11 16:24:25
 */
@Data
@TableName("isp_user")
public class IspUser {
    private static final long serialVersionUID = 247751226047278361L;

    @TableId(value = "id")
    private String userId;

    /**
     * 'vpn登录密码'
     */
    private String ispPwd;


    private String pwd;
    /**
     * 省/市/区
     */
    private String area;
    /**
     * 用于qq邮件提醒
     */
    private String qq;

    private Integer isSuccess;


}

