package com.hcs.soundboard.controller;

import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.service.SecurityService;
import com.hcs.soundboard.service.SoundboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    @Autowired
    protected SoundboardService soundboardService;

    @Autowired
    protected SecurityService securityService;

    @ModelAttribute("user")
    protected HCSUser getUser() {
        return securityService.getUser();
    }
}
