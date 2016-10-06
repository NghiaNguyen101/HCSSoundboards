package com.hcs.soundboard.data;

public enum Role {
    ROLE_ANONYMOUS, // A user is anonymous if they are not logged
    ROLE_USER, // If a user is logged in
    ROLE_ADMIN // If a user is an admin
    // The only way to be an admin is to manually edit the database.
}
