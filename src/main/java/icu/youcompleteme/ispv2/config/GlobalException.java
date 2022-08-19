package icu.youcompleteme.ispv2.config;

import icu.youcompleteme.ispv2.enums.MessageEnum;
import icu.youcompleteme.ispv2.util.BaseResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * projectName : app5_boot4DepEmp
 * className: GlobalException
 * description: TODO 类描述
 *
 * @author : chengjiang@asiainfo.com
 * @date: 2022/07/11
 */

@ControllerAdvice
public class GlobalException {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public BaseResponse exception(){

        return BaseResponse.response(MessageEnum.ERROR);
    }
}
