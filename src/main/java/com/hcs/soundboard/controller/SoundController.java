package com.hcs.soundboard.controller;

import com.hcs.soundboard.data.Sound;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SoundController extends BaseController {
    @RequestMapping("/sound/{id:.+}")
    public @ResponseBody ResponseEntity<InputStreamResource> getSound(@PathVariable int id, HttpServletResponse response) throws IOException {
        Sound sound = dao.getSoundFile(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(sound.getSize());
        response.setHeader("Cache-Control", "public, max-age=604800");
        response.setHeader("Pragma", "");
        response.setHeader("Expires", "");

        InputStreamResource resource = new InputStreamResource(sound.getSound());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @RequestMapping("/sounds")
    public ModelAndView getAllSounds() {
        ModelAndView mav = new ModelAndView("sounds");
        mav.addObject("sounds", dao.getAllSounds());
        return mav;
    }
}
