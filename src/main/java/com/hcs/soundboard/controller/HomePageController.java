package com.hcs.soundboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController extends BaseController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
