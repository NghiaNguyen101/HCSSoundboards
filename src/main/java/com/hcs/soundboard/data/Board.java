package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class Board {
    private int id;
    private String ownerName; // The username of the user who created this board.
    private boolean isHidden; // Whether the board shows up in lists on the website or not.
    private Date createDate; // The date and time the board was created.

    private BoardVersion unsharedVersion;
    private BoardVersion sharedVersion;

    public boolean hasBeenShared() {
        return sharedVersion != null;
    }

    public boolean hasUnsharedChanges() {
        return !hasBeenShared() ||
                !unsharedVersion.getUpdateDate().equals(sharedVersion.getUpdateDate());
    }
}
