package com.hcs.soundboard.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BoardCreationController extends BaseController {
    @RequestMapping("/create")
    public String newBoard() {
        return "create";
    }

    @RequestMapping(value = "/upload/sound", method = RequestMethod.POST)
    public String upload(@RequestParam MultipartFile sound, @RequestParam(required = false) String name) throws IOException {
        if (StringUtils.isEmpty(name)) {
            String filename = sound.getOriginalFilename();
            name = filename.substring(0, filename.lastIndexOf("."));
        }
        dao.uploadSound(sound.getInputStream(), name, sound.getSize());
        return "redirect:/create";
    }
}
