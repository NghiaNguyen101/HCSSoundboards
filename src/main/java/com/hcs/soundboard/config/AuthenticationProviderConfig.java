package com.hcs.soundboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

@Configuration
public class AuthenticationProviderConfig {
    @Autowired
    DataSource dataSource;

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl jdbcImpl = new JdbcDaoImpl();
        jdbcImpl.setDataSource(dataSource);
        jdbcImpl.setUsersByUsernameQuery("select username, password, enabled from User where username=?");
        jdbcImpl.setAuthoritiesByUsernameQuery("select b.username, a.role from Role a, User b where b.username=? and a.userId=b.Id");
        return jdbcImpl;
    }
}