package com.hcs.soundboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class handles requests for authentication-related URLs.
 */
@Controller
public class AuthController extends BaseController {
    /**
     * Handles requests for the sign-in page
     * @return "login"
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String auth() {
        return "login";
    }

    /**
     * Handles requests for the sign-up page.
     * @return "register"
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    /**
     * This URL is hit when the user submits the sign-up form. It signs the
     * user up, logs them in, and then redirects them to the home page.
     *
     * @param username The username the user typed in when registering.
     * @param password The password the user typed in when registering.
     * @return A redirect to the home page.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@RequestParam String username, @RequestParam String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Nonempty username and password");
        }
        securityService.registerUser(username, password);
        securityService.autoLogin(username, password);
        return "redirect:/";
    }

    /**
     * Check if the username exist by calling ajax, this will get called when user input username
     * @param username the username user wants to register
     *
     */
    @RequestMapping (value="/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public String checkUsername(@RequestParam String username){
        if(securityService.checkUsernameAjax(username))
            return "Taken";
        return "Good";
    }
}
