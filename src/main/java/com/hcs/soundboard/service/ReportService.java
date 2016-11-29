package com.hcs.soundboard.service;

import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.data.Report;
import com.hcs.soundboard.db.ReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ReportDAO reportDao;

    public void reportSoundboard(HCSUser reporter, int boardId, String reportDesc) {
        reportDao.reportSoundboard(reporter.getUsername(), boardId, reportDesc);
    }

    public List<Report> getReports(Boolean resolved) {
        return reportDao.getAllReports(resolved);
    }

    public Report getReport(int reportId) {
        return reportDao.getReport(reportId);
    }

    public void resolveReport(int reportId) {
        reportDao.resolveReport(reportId);
    }

    public void saveNotes(int reportId, String notes) {
        reportDao.saveNotes(reportId, notes);
    }
}
