package com.hcs.soundboard.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handler error page
 * Replace the whitelabel error page from spring with approriate error page
 *
 */

@Controller
public class ExceptionHandlerController implements ErrorController{

    private static final String PATH = "/error";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = PATH)
    public String error() {

        return "404";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
