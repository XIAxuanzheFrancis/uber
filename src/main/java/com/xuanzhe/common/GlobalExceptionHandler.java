package com.xuanzhe.common;

import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
    log.error(e.getMessage());
    if(e.getMessage().contains("Duplicate entry")){
      String[] s = e.getMessage().split(" ");
      String msg = s[2] + " already existing";
      return R.error(msg);
    }
    return R.error("unknown error");
  }
}
