package com.hcs.soundboard.controller;

import com.hcs.soundboard.exception.ForbiddenException;
import com.hcs.soundboard.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Hanlde all execptions here
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Handle board not found exception
     */
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView NotFound(){
        System.out.println("In NOT FOUND");
        return new ModelAndView("404", "msg", "Board not found on Server!");
    }

    /**
     * Handle forbidden exception for editing board
     */
    @ExceptionHandler(ForbiddenException.class)
    public ModelAndView Forbidden(){
        System.out.println("In Forbidden");
        return new ModelAndView("404", "msg", "You do not have the authorization to edit!");
    }
}
