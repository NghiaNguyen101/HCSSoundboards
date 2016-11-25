package com.hcs.soundboard.db;

import com.hcs.soundboard.data.*;
import com.hcs.soundboard.exception.NotFoundException;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Handles adding and retrieving sound- and sounboard-related information
 * to and from the database.
 */
@Component
public class SoundboardDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Gets the sound file BLOB from the the DB.
     *
     * @param soundId The boardId of the sound to get.
     * @return The corresponding SoundFile
     * @throws IOException Shouldn't happen
     */
    @Transactional
    public SoundFile getSoundFile(int soundId) throws IOException {
        return jdbcTemplate.queryForObject(
                "SELECT sound, size FROM sound WHERE sound.id=?",
                new Object[]{soundId},
                (rs, rn) -> new SoundFile(soundId, rs.getBinaryStream("sound"), rs.getInt("size")));
    }

    /**
     * Gets soundboard metadata as well as optionally a list of sound
     * metadata for all sounds on the board.
     *
     * @param boardId The boardId of the board in question.
     * @return The board in question.
     */
    @Transactional
    public Board getBoard(int boardId, boolean getUnsharedSounds, boolean getSharedSounds) {
        try {
            Board board = jdbcTemplate.queryForObject("SELECT board.id, ownerName, hidden, createDate, " +
                            "u.title, u.description, u.updateDate, " +
                            "s.title, s.description, s.updateDate " +
                            "FROM board " +
                            "JOIN board_version u ON board.id = u.boardId AND NOT u.shared " +
                            "LEFT JOIN board_version s ON board.id = s.boardId AND s.shared " +
                            "WHERE board.id = ?",
                    new Object[]{boardId},
                    this::boardMapper);

            if (getUnsharedSounds) {
                board.getUnsharedVersion().setSounds(getAllSoundsForBoardVersion(boardId, false));
            }
            if (getSharedSounds && board.hasBeenShared()) {
                board.getSharedVersion().setSounds(getAllSoundsForBoardVersion(boardId, true));
            }

            return board;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }

    }

    @Transactional
    public List<Board> getAllBrowsableBoards() {
        return jdbcTemplate.query("SELECT board.id, ownerName, hidden, createDate, " +
                        "u.title, u.description, u.updateDate, " +
                        "s.title, s.description, s.updateDate " +
                        "FROM board " +
                        "JOIN board_version u ON board.id = u.boardId AND NOT u.shared " +
                        "JOIN board_version s ON board.id = s.boardId AND s.shared " +
                        "WHERE NOT board.hidden " +
                        "ORDER BY s.updateDate DESC",
                this::boardMapper);
    }

    private Board boardMapper(ResultSet rs, int rn) throws SQLException {
        return new Board(
                rs.getInt("board.id"),
                rs.getString("ownerName"),
                rs.getBoolean("hidden"),
                rs.getTimestamp("createDate"),
                new BoardVersion(
                        rs.getInt("board.id"),
                        null,
                        rs.getString("u.title"),
                        rs.getString("u.description"),
                        false,
                        rs.getTimestamp("u.updateDate")),
                rs.getString("s.title") == null ? null :
                        new BoardVersion(
                                rs.getInt("board.id"),
                                null,
                                rs.getString("s.title"),
                                rs.getString("s.description"),
                                true,
                                rs.getTimestamp("s.updateDate")));
    }

    private void updateBoardVersion(int boardId) {
        jdbcTemplate.update("UPDATE board_version SET updateDate = now() " +
                        "WHERE boardId = ? AND shared = FALSE",
                boardId);
    }

    /**
     * Gets sound metadata for all the sounds on a particular board.
     *
     * @param boardId The boardId of the board in question.
     * @return Sound metadata for all sounds on the board.
     */
    @Transactional
    private List<SoundMetadata> getAllSoundsForBoardVersion(int boardId, boolean shared) {
        return jdbcTemplate.query(
                "SELECT id, soundName FROM sound JOIN board_x_sound ON sound.id = board_x_sound.soundId " +
                        "WHERE boardId = ? AND shared = ?",
                new Object[]{boardId, shared},
                (rs, rownum) -> new SoundMetadata(rs.getInt("id"), rs.getString("soundName")));
    }

    /**
     * Adds all the sound file to a board, with the given names.
     *
     * @param sounds  The SoundFiles for each sound to add.
     * @param names   The names of the sounds, parallel to the sounds list.
     * @param boardId The boardId of the board in question.
     * @return A list containing the ids of the sounds which have been added.
     */
    @Transactional
    public List<Integer> addSoundsToBoard(List<SoundFile> sounds, List<String> names, int boardId) {
        List<Object[]> soundArgs = sounds.stream()
                .map(s -> new Object[]{s.getSound(), s.getSize()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO sound (sound, size) VALUE (?, ?)",
                soundArgs);

        List<Integer> soundIds = jdbcTemplate.queryForList(
                "SELECT id FROM (SELECT id FROM sound ORDER BY id DESC LIMIT ?) ids ORDER BY id ASC",
                new Object[]{sounds.size()},
                Integer.class);

        List<Object[]> boardXSoundArgs = IntStream.range(0, soundIds.size())
                .mapToObj(i -> new Object[]{boardId, soundIds.get(i), names.get(i)})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO board_x_sound (boardId, soundId, soundName, shared) " +
                        "VALUE (?, ?, ?, FALSE)",
                boardXSoundArgs);

        updateBoardVersion(boardId);

        return soundIds;
    }

    /**
     * Removes sounds from the soundboard
     *
     * @param soundIds The ids of the sounds to remove.
     * @param boardId  The boardId of the board in question.
     * @return The soundId of the removed sound.
     */
    public List<Integer> removeSoundsFromBoard(List<Integer> soundIds, int boardId) {
        List<Object[]> boardXSoundArgs = IntStream.range(0, soundIds.size())
                .mapToObj(i -> new Object[]{boardId, soundIds.get(i)})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("DELETE FROM board_x_sound WHERE boardId = ? " +
                        "AND soundId = ? AND shared = FALSE",
                boardXSoundArgs);
        updateBoardVersion(boardId);
        return soundIds;
    }

    /**
     * Change name of sounds in soundboard
     *
     * @param metadatas The metadata of the sounds
     * @param boardId   The boardId of the board in question.
     */
    public void editSoundNames(List<SoundMetadata> metadatas, int boardId) {
        List<Object[]> boardXSoundArgs = metadatas.stream()
                .map(metadata -> new Object[]{metadata.getName(), boardId, metadata.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("UPDATE board_x_sound SET soundName = ? WHERE boardId = ? " +
                        "AND soundId = ? AND shared = FALSE",
                boardXSoundArgs);
        updateBoardVersion(boardId);
    }

    /**
     * Change board name and board description
     * @param boardId The boardId of the board in question.
     * @param boardName The new board name
     * @param boardDesc The new board description
     */
    @Transactional
    public void editBoardDesc(int boardId, String boardName, String boardDesc) {
        jdbcTemplate.update("UPDATE board_version SET title = ?, description = ? " +
                "WHERE boardId = ? AND shared = FALSE", boardName, boardDesc, boardId);
    }

    /**
     * Creates a new board record with the given owner
     *
     * @param owner       The username of the user who is creating the board.
     * @param title       The title of the new soundboard
     * @param description The user's description of the soundboard
     * @return The boardId of the new board.
     */
    @Transactional
    public int createSoundboard(String owner, String title, String description) {
        jdbcTemplate.update("INSERT INTO board (ownerName, createDate, hidden) " +
                        "VALUE (?, now(), FALSE)",
                owner);

        int boardId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Integer.class);

        jdbcTemplate.update("INSERT INTO board_version (boardId, shared, title, description, updateDate) " +
                        "VALUE (?, FALSE, ?, ?, now())",
                boardId, title, description);

        return boardId;
    }

    @Transactional
    public void shareBoard(int boardId) {
        jdbcTemplate.update("DELETE FROM board_version WHERE boardId = ? AND shared = TRUE", boardId);

        jdbcTemplate.update("DELETE FROM board_x_sound WHERE boardId = ? AND shared = TRUE", boardId);

        jdbcTemplate.update("UPDATE board_version SET updateDate = now() " +
                "WHERE boardId = ? AND shared = FALSE", boardId);

        jdbcTemplate.update("INSERT INTO board_version (boardId, shared, title, description, updateDate) " +
                "SELECT boardId, TRUE, title, description, updateDate FROM board_version " +
                "WHERE boardId = ? AND shared = FALSE", boardId);

        jdbcTemplate.update("INSERT INTO board_x_sound (boardId, shared, soundId, soundName)" +
                "SELECT boardId, TRUE, soundId, soundName FROM board_x_sound " +
                "WHERE boardId = ? AND shared = FALSE", boardId);
    }

    /**
     * Gets metadata for all of the soundboards a user owns, for both shared and unshared versions.
     *
     * @param username The username of the user for whom we are getting boards.
     * @return A list of all the boards a user has created, public or not. The
     * sounds field is NOT populated.
     */
    @Transactional
    public List<Board> getUsersBoards(String username) {
        return jdbcTemplate.query("SELECT board.id, ownerName, hidden, createDate, " +
                        "u.title, u.description, u.updateDate, " +
                        "s.title, s.description, s.updateDate " +
                        "FROM board " +
                        "JOIN board_version u ON board.id = u.boardId AND NOT u.shared " +
                        "LEFT JOIN board_version s ON board.id = s.boardId AND s.shared " +
                        "WHERE ownerName = ? " +
                        "ORDER BY u.updateDate DESC",
                new Object[]{username},
                this::boardMapper);
    }

    @Transactional
    public List<Board> getUsersPublicBoards(String username) {
        return jdbcTemplate.query("SELECT board.id, ownerName, hidden, createDate, " +
                        "u.title, u.description, u.updateDate, " +
                        "s.title, s.description, s.updateDate " +
                        "FROM board " +
                        "JOIN board_version u ON board.id = u.boardId AND NOT u.shared " +
                        "JOIN board_version s ON board.id = s.boardId AND s.shared " +
                        "WHERE ownerName = ? AND NOT board.hidden " +
                        "ORDER BY s.updateDate DESC",
                new Object[]{username},
                this::boardMapper);
    }

    /*
     * Report the soundboard
     *
     * Create a new entry in table report_board with column resolved is 0
     */
    @Transactional
    public void reportSoundBoard(String reportUser, int boardId, String reportTitle, String reportDesc){
        String boardOwner = jdbcTemplate.queryForObject("SELECT ownerName FROM board WHERE id=?",
                new Object[]{boardId} ,String.class);
        String boardTitle = jdbcTemplate.queryForObject("SELECT title FROM board_version WHERE boardId=? " +
                        "AND shared=1", new Object[]{boardId}, String.class);

        jdbcTemplate.update("INSERT  INTO report_board (boardId, boardTitle, reportUser, boardOwner ,reportTitle," +
                " reportDesc, reportDate) VALUE (?, ?,  ?, ?, ?, ?, NOW())",
                boardId, boardTitle ,reportUser, boardOwner, reportTitle, reportDesc);
    }


    /*
     * Get all unresolved reports
     *
     * @return list of report
     */
    public List<Report> getAllReports(){
        return jdbcTemplate.query("SELECT reportId, boardId, boardTitle, reportUser, boardOwner, reportTitle, reportDesc, reportDate, notes" +
                " FROM report_board WHERE resolved=0 ORDER BY reportDate DESC", this::reportMapper);
    }

    /*
     * Get the report in question
     * @param reportId  the id of the report in question
     * @return the report in question
     */
    public Report getReport(int reportId){
        return jdbcTemplate.queryForObject("SELECT reportId, boardId, boardTitle, reportUser, boardOwner, reportTitle, reportDesc, reportDate, notes" +
                " FROM report_board WHERE reportId=?", new Object[]{reportId}, this::reportMapper);
    }

    /*
     * Update the report to resolved
     *
     * @param reportId the id of the report to be resolved
     */
    public void resolvedReport(int reportId){
        jdbcTemplate.update("UPDATE report_board SET resolved=1 WHERE reportId=?", reportId);
    }

    private Report reportMapper(ResultSet rs, int rn) throws SQLException {
        return new Report(rs.getInt("reportId"),
                          rs.getInt("boardId"),
                          rs.getString("boardTitle"),
                          rs.getString("reportUser"),
                          rs.getString("boardOwner"),
                          rs.getString("reportTitle"),
                          rs.getString("reportDesc"),
                          rs.getTimestamp("reportDate"),
                          rs.getString("notes"));
    }

    @Transactional
    public void saveNotesReport(int reportId, String notes){
        jdbcTemplate.update("UPDATE report_board SET notes=? WHERE reportId=?", notes, reportId);
    }
}
