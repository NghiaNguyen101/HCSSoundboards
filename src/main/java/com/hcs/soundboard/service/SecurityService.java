package com.hcs.soundboard.service;

import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.data.Role;
import com.hcs.soundboard.db.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AccountDAO accountDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public HCSUser getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        boolean isAnonymous = roles.contains(Role.ROLE_ANONYMOUS.name());
        boolean isMember = roles.contains(Role.ROLE_USER.name());
        boolean isAdmin = roles.contains(Role.ROLE_ADMIN.name());

        if (isAnonymous) {
            username = null;
        }

        return new HCSUser(isAnonymous, isMember, isAdmin, username);
    }

    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    public void registerUser(String username, String password) {
        String hashedPassword = encoder.encode(password);
        accountDao.registerUser(username, hashedPassword);
    }
}