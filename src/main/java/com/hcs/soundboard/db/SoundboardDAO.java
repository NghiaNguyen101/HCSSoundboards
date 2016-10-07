package com.hcs.soundboard.db;

import com.hcs.soundboard.data.Board;
import com.hcs.soundboard.data.SoundFile;
import com.hcs.soundboard.data.SoundMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
     * @param soundId The id of the sound to get.
     * @return The corresponding SoundFile
     * @throws IOException Shouldn't happen
     */
    @Transactional
    public SoundFile getSoundFile(int soundId) throws IOException {
        return jdbcTemplate.queryForObject(
                "select sound, size from sound where sound.id=?",
                new Object[] {soundId},
                (rs, rn) -> new SoundFile(soundId, rs.getBinaryStream("sound"), rs.getInt("size")));
    }

    /**
     * Gets soundboard metadata as well as optionally a list of sound
     * metadata for all sounds on the board.
     * @param boardId The id of the board in question.
     * @param getSounds Whether or not the sounds field of the Board object
     *                  should be populated. When fetching a board to see who
     *                  owner is, we don't actually care about the sounds, so we
     *                  we don't waste time getting them.
     * @return The board in question.
     */
    @Transactional
    public Board getBoard(int boardId, boolean getSounds) {
        return jdbcTemplate.queryForObject(
                "select name, description, username, public, createDate " +
                        "from board join user on board.ownerId = user.id where board.id = ?",
                new Object[] {boardId},
                (rs, rn) -> new Board(boardId,
                        getSounds ? getAllSoundsForBoard(boardId) : null,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("public"),
                        rs.getDate("createDate")));
    }

    /**
     * Gets sound metadata for all the sounds on a particular board.
     * @param boardId The id of the board in question.
     * @return Sound metadata for all sounds on the board.
     */
    @Transactional
    private List<SoundMetadata> getAllSoundsForBoard(int boardId) {
        return jdbcTemplate.query(
                "select id, soundName, public from sound join board_x_sound on sound.id = board_x_sound.soundId " +
                        "where boardId = ?",
                new Object[] {boardId},
                (rs, rownum) -> new SoundMetadata(rs.getInt("id"), rs.getString("soundName"), rs.getBoolean("public")));
    }

    /**
     * Adds all the sound file to a board, with the given names.
     * @param sounds The SoundFiles for each sound to add.
     * @param names The names of the sounds, parallel to the sounds list.
     * @param boardId The id of the board in question.
     * @return A list containing the ids of the sounds which have been added.
     */
    @Transactional
    public List<Integer> addSoundsToBoard(List<SoundFile> sounds, List<String> names, int boardId) {
        List<Object[]> soundArgs = sounds.stream()
                .map(s -> new Object[] {s.getSound(), s.getSize()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into sound (sound, size) VALUE (?, ?)",
                soundArgs);

        List<Integer> soundIds = jdbcTemplate.queryForList(
                "select id from (select id from sound order by id desc limit ?) ids order by id asc",
                new Object[] {sounds.size()},
                Integer.class);

        List<Object[]> boardXSoundArgs = IntStream.range(0, soundIds.size())
                .mapToObj(i -> new Object[] {boardId, soundIds.get(i), names.get(i)})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into board_x_sound (boardId, soundId, soundName, public) " +
                        "VALUE (?, ?, ?, false)",
                boardXSoundArgs);
        return soundIds;
    }

    /**
     * Creates a new board record with the given owner
     * @param owner The username of the user who is creating the board.
     * @param title The title of the new soundboard
     * @param description The user's description of the soundboard
     * @return The id of the new board.
     */
    @Transactional
    public int createSoundboard(String owner, String title, String description) {
        jdbcTemplate.update("insert into board (name, description, ownerId, public, createDate) " +
                        "VALUE (?, ?, (select id from user where username=?), false, now())",
                title, description, owner);
        return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
    }

    /**
     * Gets metadata for all of the soundboards a user owns.
     * @param username The username of the user for whom we are getting boards.
     * @return A list of all the boards a user has created, public or not. The
     * sounds field is NOT populated.
     */
    @Transactional
    public List<Board> getUsersBoards(String username) {
        // TODO Order the boards by createDate or something.
        return jdbcTemplate.query(
                "select board.id, name, description, username, public, createDate " +
                        "from board join user on board.ownerId = user.id where user.username = ?",
                new Object[] {username},
                (rs, rn) -> new Board(rs.getInt("board.id"),
                        null,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("public"),
                        rs.getDate("createDate")));
    }
}
