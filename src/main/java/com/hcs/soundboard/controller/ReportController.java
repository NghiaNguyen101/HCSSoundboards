package com.hcs.soundboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class ReportController extends BaseController {
    /**
     * This URL hit when a user reports a board
     *
     * @param boardId     The board in question
     * @param reportDesc  The description of the report
     */
    @RequestMapping(value = "/board/{boardId:.+}/create-report", method = RequestMethod.POST)
    public String reportBoard(@PathVariable int boardId,
                              @RequestParam("reportDesc") String reportDesc) throws IOException {
        System.out.println(reportDesc);
        reportService.reportSoundboard(getUser(), boardId, reportDesc);
        return "redirect:/board/" + boardId;
    }

    @RequestMapping("/all-report")
    public ModelAndView getReports(@RequestParam(required = false) Boolean resolved) {
        ModelAndView mav = new ModelAndView("all-report");
        mav.addObject("reports", reportService.getReports(resolved));
        return mav;
    }

    @RequestMapping(value = "/report/{reportId:.+}")
    public ModelAndView handleReport(@PathVariable int reportId) {
        ModelAndView mav = new ModelAndView("report");
        mav.addObject("report", reportService.getReport(reportId));
        return mav;
    }

    @RequestMapping(value = "report/{reportId:.+}/resolve", method = RequestMethod.POST)
    public String resolvedReport(@PathVariable int reportId) {
        reportService.resolveReport(reportId);
        return "redirect:/all-report";
    }

    @RequestMapping(value = "report/{reportId:.+}/save_notes", method = RequestMethod.POST)
    public String saveNotesReport(@PathVariable int reportId, @RequestParam String notes) {
        reportService.saveNotes(reportId, notes);
        return "redirect:/report/" + reportId;
    }
}
