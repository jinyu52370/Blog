package com.jinyu.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/5/30 18:22
 */
@ControllerAdvice
public class ControllerExceptionHandler{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Model model, HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(), e);

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        model.addAttribute("url", request.getRequestURL());
        model.addAttribute("exception", e);
        return "error/error";
    }
}
