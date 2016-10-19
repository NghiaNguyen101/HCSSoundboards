package com.hcs.soundboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Handler error page
 * Replace the whitelabel error page from spring with approriate error page
 * Capture any error
 */

@Controller
@RequestMapping("${error.path:/error}")
public class CustomErrorController implements ErrorController{

    @Value("${error.path:/error}")
    private String errorPath;

    @Override
    public String getErrorPath() {
        return this.errorPath;
    }

    /**
     * Get HttpStatus code
     * Based on the code return approriate error page
     * For now is 404
     * @return the 404 page with message
     */
    @RequestMapping
    public ModelAndView error(HttpServletRequest request){
        HttpStatus status = getStatus(request);
        System.out.println(status.value());
        return new ModelAndView("404", "msg", "URL not found on Server");
    }

    /**
     * @return HttpStatus code
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
