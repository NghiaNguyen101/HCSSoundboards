package com.hcs.soundboard.db;

import com.hcs.soundboard.data.Board;
import com.hcs.soundboard.data.BoardVersion;
import com.hcs.soundboard.data.SoundFile;
import com.hcs.soundboard.data.SoundMetadata;
import com.hcs.soundboard.exception.NotFoundException;
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
                            "WHERE board.id = ? AND board.deleteDate IS NULL",
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
                        "WHERE NOT board.hidden AND deleteDate IS NULL " +
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
                "SELECT id, soundName, boxColor FROM sound JOIN board_x_sound ON sound.id = board_x_sound.soundId " +
                        "WHERE boardId = ? AND shared = ?",
                new Object[]{boardId, shared},
                (rs, rownum) -> new SoundMetadata(rs.getInt("id"), rs.getString("soundName"), rs.getString("boxColor")));
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
    public void editSounds(List<SoundMetadata> metadatas, int boardId) {
        List<Object[]> boardXSoundArgs = metadatas.stream()
                .map(metadata -> new Object[]{metadata.getName(), metadata.getBoxColor() ,boardId, metadata.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("UPDATE board_x_sound SET soundName = ?, boxColor = ? WHERE boardId = ? " +
                        "AND soundId = ? AND shared = FALSE",
                boardXSoundArgs);
        updateBoardVersion(boardId);
    }

    /**
     * Change board name and board description
     *
     * @param boardId   The boardId of the board in question.
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

        jdbcTemplate.update("INSERT INTO board_x_sound (boardId, shared, soundId, soundName, boxColor)" +
                "SELECT boardId, TRUE, soundId, soundName, boxColor FROM board_x_sound " +
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
                        "WHERE ownerName = ? AND deleteDate IS NULL " +
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
                        "WHERE ownerName = ? AND NOT board.hidden AND board.deleteDate IS NULL " +
                        "ORDER BY s.updateDate DESC",
                new Object[]{username},
                this::boardMapper);
    }

    @Transactional
    public void deleteBoard(int boardId) {
        jdbcTemplate.update("UPDATE board SET deleteDate = NOW() WHERE id = ?", boardId);
    }

    @Transactional
    public void setHidden(int boardId, boolean hidden) {
        jdbcTemplate.update("update board set hidden = ? where id = ?", hidden, boardId);
    }
}
