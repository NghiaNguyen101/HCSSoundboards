package com.hcs.soundboard.db;

import com.hcs.soundboard.data.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReportDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Report the soundboard
     *
     * Create a new entry in table report_board with column resolved is 0
     */
    @Transactional
    public void reportSoundboard(String reportUser, int boardId, String reportDesc) {
        String boardTitle = jdbcTemplate.queryForObject("SELECT title FROM board_version WHERE boardId=? " +
                "AND shared=1", new Object[]{boardId}, String.class);

        jdbcTemplate.update("INSERT INTO report_board (boardId, boardTitle, reportUser," +
                        " reportDesc, reportDate) VALUE (?, ?, ?, ?, NOW())",
                boardId, boardTitle, reportUser, reportDesc);
    }


    /**
     * Get all reports
     *
     * @return list of report
     */
    public List<Report> getAllReports(Boolean resolved) {
        return jdbcTemplate.query("SELECT reportId, boardId, boardTitle, reportUser, ownerName, reportDesc, reportDate, notes " +
                        "FROM report_board join board on boardId = board.id WHERE COALESCE(resolved = ?, TRUE) ORDER BY reportDate DESC",
                new Object[]{resolved},
                this::reportMapper);
    }

    /**
     * Get the report in question
     *
     * @param reportId the id of the report in question
     * @return the report in question
     */
    public Report getReport(int reportId) {
        return jdbcTemplate.queryForObject("SELECT reportId, boardId, boardTitle, reportUser, ownerName, reportDesc, reportDate, notes " +
                "FROM report_board join board on boardId = board.id WHERE reportId=?", new Object[]{reportId}, this::reportMapper);
    }

    /**
     * Update the report to resolved
     *
     * @param reportId the id of the report to be resolved
     */
    public void resolveReport(int reportId) {
        jdbcTemplate.update("UPDATE report_board SET resolved=1 WHERE reportId=?", reportId);
    }

    private Report reportMapper(ResultSet rs, int rn) throws SQLException {
        return new Report(rs.getInt("reportId"),
                rs.getInt("boardId"),
                rs.getString("boardTitle"),
                rs.getString("reportUser"),
                rs.getString("ownerName"),
                rs.getString("reportDesc"),
                rs.getTimestamp("reportDate"),
                rs.getString("notes"));
    }

    @Transactional
    public void saveNotes(int reportId, String notes) {
        jdbcTemplate.update("UPDATE report_board SET notes=? WHERE reportId=?", notes, reportId);
    }
}
