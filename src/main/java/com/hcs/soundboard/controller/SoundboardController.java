package com.hcs.soundboard.controller;

import com.hcs.soundboard.data.SoundFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SoundboardController extends BaseController {
    @RequestMapping("/sound/{soundId:.+}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getSound(
            @PathVariable int soundId, HttpServletResponse response) throws IOException {
        SoundFile sound = soundboardService.getSound(soundId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(sound.getSize());
        response.setHeader("Cache-Control", "public, max-age=3600");
        response.setHeader("Pragma", "");
        response.setHeader("Expires", "");

        InputStreamResource resource = new InputStreamResource(sound.getSound());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @RequestMapping("/board/{boardId:.+}")
    public ModelAndView viewBoard(@PathVariable int boardId) {
        ModelAndView mav = new ModelAndView("board");
        mav.addObject("board", soundboardService.getBoardForViewing(getUser(), boardId));
        return mav;
    }

    @RequestMapping("/create")
    public String newBoard() {
        int boardId = soundboardService.createSoundboard(getUser());
        return String.format("redirect:/board/%d/edit", boardId);
    }

    @RequestMapping("/board/{boardId:.+}/edit")
    public ModelAndView editBoard(@PathVariable int boardId) {
        ModelAndView mav = new ModelAndView("edit");
        mav.addObject("board", soundboardService.getBoardForEditing(getUser(), boardId));
        return mav;
    }

    @RequestMapping("/your-boards")
    public ModelAndView yourBoards() {
        ModelAndView mav = new ModelAndView("your-boards");
        mav.addObject("boards", soundboardService.getUsersBoards(getUser()));
        return mav;
    }

    @RequestMapping(value = "/board/{boardId:.+}/upload", method = RequestMethod.POST)
    public String upload(@PathVariable int boardId,
                      @RequestParam List<MultipartFile> sounds) throws IOException {
        List<SoundFile> soundFiles = sounds.stream()
                .map(s -> new SoundFile(0, getInputStream(s), s.getSize()))
                .collect(Collectors.toList());

        List<String> names = sounds.stream().map(s -> {
            String filename = s.getOriginalFilename();
            return filename.substring(0, filename.lastIndexOf("."));
        }).collect(Collectors.toList());

        soundboardService.addSoundsToBoard(getUser(), soundFiles, names, boardId);
        return String.format("redirect:/board/%d/edit", boardId);
    }

    private InputStream getInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
