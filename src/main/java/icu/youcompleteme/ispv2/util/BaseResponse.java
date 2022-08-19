package icu.youcompleteme.ispv2.util;

import icu.youcompleteme.ispv2.enums.MessageEnum;
import lombok.Data;

/**
 * projectName : app3_spring2ibatis
 * className: BaseResponse
 * description: TODO 类描述
 *
 * @author : git.jas0nch
 * date: 2022-03-14
 */

@Data
public class BaseResponse<T> {

    private int code = 0;

    private String message = "";

    // token ?

    private T data;

    public static <T> BaseResponse response(T data, MessageEnum msg) {

        BaseResponse<T> jsonData = new BaseResponse<>();
        jsonData.setCode(msg.getCode());
        jsonData.setData(data);
        jsonData.setMessage(msg.getMessage());
        return jsonData;
    }

    public static <T> BaseResponse response(MessageEnum msg) {

        return response(null, msg);
    }
}
