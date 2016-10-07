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

/**
 * This class handles requests for the meat of the application:
 * sounds and sounboards.
 */
@Controller
public class SoundboardController extends BaseController {
    /**
     * This method handles a request for a particular sound clip.
     * @param soundId The id of the sound to be fetched.
     * @param response This param is necessary so we can manually change the
     *                 headers on the response to enable caching.
     * @return The sound clip
     * @throws IOException This shouldn't happen
     */
    @RequestMapping("/sound/{soundId:.+}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getSound(
            @PathVariable int soundId, HttpServletResponse response) throws IOException {
        SoundFile sound = soundboardService.getSound(soundId);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(sound.getSize());
        // Setting these headers allows the clip to be cached, so the browser
        // only needs to request the sound once. Cache for 1 hour.
        response.setHeader("Cache-Control", "public, max-age=3600");
        response.setHeader("Pragma", "");
        response.setHeader("Expires", "");

        InputStreamResource resource = new InputStreamResource(sound.getSound());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * Handles requests for viewing a board.
     * @param boardId the id of the board to be viewed.
     * @return board.jsp
     */
    @RequestMapping("/board/{boardId:.+}")
    public ModelAndView viewBoard(@PathVariable int boardId) {
        ModelAndView mav = new ModelAndView("board");
        mav.addObject("board", soundboardService.getBoardForViewing(getUser(), boardId));
        return mav;
    }

    /**
     * The page to create a new soundboard
     * @return "create"
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createBoard() {
        return "create";
    }

    /**
     * Handles requests to create a soundboard with a title and description.
     *
     * @param title The title the user is giving the soundboard
     * @param description then description the user is giving the soundboard
     * @return A redirect to the edit page.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String newBoard(@RequestParam String title, @RequestParam String description) {
        int newBoardId = soundboardService.createSoundboard(getUser(), title, description);
        return String.format("redirect:/board/%d/edit", newBoardId);
    }

    /**
     * This handles requests to get the board-edit page.
     *
     * @param boardId The id of the board to be edited.
     * @return the board-edit page
     */
    @RequestMapping("/board/{boardId:.+}/edit")
    public ModelAndView editBoard(@PathVariable int boardId) {
        ModelAndView mav = new ModelAndView("edit");
        mav.addObject("board", soundboardService.getBoardForEditing(getUser(), boardId));
        return mav;
    }

    /**
     * Handles a request for the your-boards page.
     * @return Page showing all the user's boards.
     */
    @RequestMapping("/your-boards")
    public ModelAndView yourBoards() {
        ModelAndView mav = new ModelAndView("your-boards");
        mav.addObject("boards", soundboardService.getUsersBoards(getUser()));
        return mav;
    }

    /**
     * This URL is hit when the user uploads sounds to their board.
     *
     * @param boardId The id of the board to which the sound are being added.
     * @param sounds The sound clips being uploaded.
     * @return Redirects to the board-edit page
     * @throws IOException This shouldn't happen.
     */
    @RequestMapping(value = "/board/{boardId:.+}/upload", method = RequestMethod.POST)
    public String upload(@PathVariable int boardId,
                      @RequestParam List<MultipartFile> sounds) throws IOException {
        List<SoundFile> soundFiles = sounds.stream()
                .map(s -> new SoundFile(0, getInputStream(s), s.getSize()))
                .collect(Collectors.toList());

        // Get the sound names from their original filenames, removing extension
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
