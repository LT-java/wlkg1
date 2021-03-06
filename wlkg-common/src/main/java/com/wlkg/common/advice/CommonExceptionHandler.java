package com.wlkg.common.advice;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(WlkgException.class)
    public ResponseEntity<ExceptionResult> handleException(WlkgException e){
        ExceptionEnums em = e.getExceptionEnums();
        //我们暂定返回状态码为400， 然后从异常中获取友好提示信息
        return ResponseEntity.status(em.getCode()).body(new ExceptionResult(em));
    }
}
