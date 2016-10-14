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

/**
 * This handles user account-related stuff.
 */
@Service
public class SecurityService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AccountDAO accountDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Gets the current user.
     * @return Current user.
     */
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

    /**
     * Logs the user in. Spring handles this when /login is hit, so this is just
     * for automatically logging the user in when they sign up/.
     * @param username User's username
     * @param password Unhashed password
     */
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    /**
     * Attempts to register a user with the given username and password.
     * @param username The new user's username
     * @param password The new user's password. It is hashed before being
     *                 stored in the DB.
     */
    public void registerUser(String username, String password) {
        String hashedPassword = encoder.encode(password);
        accountDao.registerUser(username, hashedPassword);
    }

    /**
     * Check username using ajax
     * @param username The new user's username
     * @return true if exist, false if not
     */
    public boolean checkUsernameAjax(String username){
        if(accountDao.doesUserExist(username))
            return true;
        return false;
    }
}