package com.hcs.soundboard.db;

import com.hcs.soundboard.data.Board;
import com.hcs.soundboard.data.SoundFile;
import com.hcs.soundboard.data.SoundMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class SoundboardDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public SoundFile getSoundFile(int soundId) throws IOException {
        return jdbcTemplate.queryForObject(
                "select sound, size from sound where sound.id=?",
                new Object[] {soundId},
                (rs, rn) -> new SoundFile(soundId, rs.getBinaryStream("sound"), rs.getInt("size")));
    }

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

    @Transactional
    private List<SoundMetadata> getAllSoundsForBoard(int boardId) {
        return jdbcTemplate.query(
                "select id, soundName, public from sound join board_x_sound on sound.id = board_x_sound.soundId " +
                        "where boardId = ?",
                new Object[] {boardId},
                (rs, rownum) -> new SoundMetadata(rs.getInt("id"), rs.getString("soundName"), rs.getBoolean("public")));
    }

    @Transactional
    public int addSoundToBoard(InputStream sound, long size, String name, int boardId) {
        jdbcTemplate.update("insert into sound (sound, size) VALUE (?, ?)",
                sound, size);
        int soundId = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
        jdbcTemplate.update("insert into board_x_sound (boardId, soundId, soundName, public) " +
                        "VALUE (?, ?, ?, false)",
                boardId, soundId, name);
        return soundId;
    }

    @Transactional
    public int createSoundboard(String owner) {
        jdbcTemplate.update("insert into board (name, description, ownerId, public, createDate) " +
                        "VALUE (?, ?, (select id from user where username=?), false, now())",
                "Untitled Soundboard", owner + "'s soundboard", owner);
        return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
    }

    @Transactional
    public List<Board> getUsersBoards(String username) {
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
