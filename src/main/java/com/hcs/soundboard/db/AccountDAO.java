package com.hcs.soundboard.db;

import com.hcs.soundboard.exception.NameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void registerUser(String username, String hashedPassword) {
        if (doesUserExist(username)) {
            throw new NameTakenException();
        }
        jdbcTemplate.update("INSERT INTO user (username, password, enabled) VALUE (?, ?, true)",
                username, hashedPassword);
        jdbcTemplate.update("INSERT into role (userId, role) VALUE ((select id from User where username = ?), 'ROLE_USER')",
                username);
    }

    private boolean doesUserExist(String username) {
        return jdbcTemplate.queryForObject("select count(*) > 0 from user where username = ?",
                new Object[] {username}, Boolean.class);
    }
}
