package icu.youcompleteme.ispv2.enums;

/**
 * projectName : app3_spring2ibatis
 * className: OperationLogEnum
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-03-28
 */

public enum OperationLogEnum {

    OTHER(1000, "正在执行操作"),

    USER_LOGIN(1001, "用户执行登录"),
    USER_LOGOUT(1002, "用户执行登出"),

    UPDATE(1010, "更新"),
    CREATE(1011, "创建"),
    QUERY(1012, "查询"),
    DELETE(1013, "删除"),
    ACCEPT(1014, "接受任务"),
    QUERY_USER_DETAIL(1015, "查询用户详情");

    private Integer code;
    private String message;

    OperationLogEnum() {
    }

    OperationLogEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
