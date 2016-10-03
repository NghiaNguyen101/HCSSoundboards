package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HCSUser {
    private boolean isAnonymous;
    private boolean isMember;
    private boolean isAdmin;
    private String username;
}
