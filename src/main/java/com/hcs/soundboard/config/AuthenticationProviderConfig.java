package com.hcs.soundboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

/**
 * This class configures the information needed for Spring to be able to handle user authentication.
 */
@Configuration
public class AuthenticationProviderConfig {
    @Autowired
    DataSource dataSource;

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
        jdbcImpl.setDataSource(dataSource);
        jdbcImpl.setUsersByUsernameQuery("select username, password, enabled from User where username = ?");
        jdbcImpl.setAuthoritiesByUsernameQuery("select username, role from Role where username = ?");
        return jdbcImpl;
    }
}