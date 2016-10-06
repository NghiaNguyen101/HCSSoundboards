package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Holds information about an HCS user (not necessarily a member)
 */
@Data @AllArgsConstructor
public class HCSUser {
    private boolean isAnonymous;
    private boolean isMember;
    private boolean isAdmin;
    private String username; // null if the user isn't logged in.
}
