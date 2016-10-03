package com.hcs.soundboard.db;

import com.hcs.soundboard.data.Sound;
import com.hcs.soundboard.exception.NameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataAccessObject {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Sound getSoundFile(int id) throws IOException {
        List<Sound> soundList = jdbcTemplate.query("select name, sound, size from Sound where id=?",
                new Object[] {id},
                (rs, rn) -> new Sound(id, rs.getString("name"), rs.getBinaryStream("sound"), rs.getInt("size")));

        return soundList.get(0);
    }

    public List<Sound> getAllSounds() {
        return jdbcTemplate.query("select id, name from Sound",
                (rs, rownum) -> new Sound(rs.getInt("id"), rs.getString("name"), null, 0));
    }

    public void uploadSound(InputStream sound, String name, long size) {
        jdbcTemplate.update("insert into sound (name, sound, size) VALUE (?, ?, ?)",
                name, sound, size);
    }

    @Transactional
    public void registerUser(String username, String hashedPassword) {
        if (doesUserExist(username)) {
            throw new NameTakenException();
        }
        jdbcTemplate.update("INSERT INTO User (username, password, enabled) VALUE (?, ?, true)",
                username, hashedPassword);
        jdbcTemplate.update("INSERT into Role (userId, role) VALUE ((select id from User where username = ?), 'ROLE_USER')",
                username);
    }

    public boolean doesUserExist(String username) {
        int count = jdbcTemplate.queryForObject("select count(*) from User where username = ?",
                new Object[] {username}, Integer.class);
        return count > 0;
    }
}
