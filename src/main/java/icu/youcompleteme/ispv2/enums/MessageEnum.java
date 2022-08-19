package icu.youcompleteme.ispv2.enums;

/**
 * projectName : app3_spring2ibatis
 * className: MessageEnum
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-03-16
 */
public enum MessageEnum {

    SUCCESS(200, "操作成功"),
    NOT_FOUND(404, "未找到相关资源"),
    ERROR(500, "未知错误"),

    PARAM_INVALID(4001, "参数异常"),
    USER_NOT_EXIST(4001, "用户不存在"),
    INVALID_USERNAME_OR_PASSWORD(4002, "用户名或密码错误"),
    REGISTER_SUCCESS(201, "注册成功"),
    USERNAME_EXIST(4003, "用户名已存在");


    private int code;
    private String message;


    MessageEnum() {
    }

    MessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param code
     * @return : java.lang.String
     * @description : 根据代码获得对应枚举的message
     * @author : git.jas0nch
     * date : 2022/3/16
     **/
    public static String name(int code) {

        MessageEnum[] messageEnums = MessageEnum.values();
        for (MessageEnum anEnum : messageEnums) {
            if (anEnum.getCode() == code) {
                return anEnum.getMessage();
            }
        }

        return "other operation";
    }
}
