package com.hcs.soundboard.controller;

import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.db.DataAccessObject;
import com.hcs.soundboard.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    @Autowired
    protected DataAccessObject dao;

    @Autowired
    protected SecurityService securityService;

    @ModelAttribute("user")
    protected HCSUser getUser() {
        return securityService.getUser();
    }
}
