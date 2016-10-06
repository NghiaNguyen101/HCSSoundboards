package com.hcs.soundboard.controller;

import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.service.SecurityService;
import com.hcs.soundboard.service.SoundboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * This class provides components common to the controllers.
 */
public class BaseController {
    @Autowired
    protected SoundboardService soundboardService;

    @Autowired
    protected SecurityService securityService;

    /**
     * This method gets the current user and makes it available in the JSP
     * without having to explicitly add it with mav.addObject().
     *
     * @return The current user.
     */
    @ModelAttribute("user")
    protected HCSUser getUser() {
        return securityService.getUser();
    }
}
