package com.hcs.soundboard.controller;

import com.hcs.soundboard.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController extends BaseController {
    @Autowired
    private SecurityService securityService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String auth() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String postRegister(@RequestParam String username, @RequestParam String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Nonempty username and password");
        }
        String hashedPassword = encoder.encode(password);
        dao.registerUser(username, hashedPassword);
        securityService.autoLogin(username, password);
        return "redirect:/";
    }
}
