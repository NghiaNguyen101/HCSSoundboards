package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * This class holds information about a single soundboard.
 */
@Data @AllArgsConstructor
public class Board {
    private int id;
    private List<SoundMetadata> sounds;
    private String ownerName; // The username of the user who created this board.
    private String boardName; // The name of the soundboard
    private String description; // The description of the soundboard
    private boolean isPublic; // Whether or not the soundboard is publicly viewable yet.
    private Date createDate; // The date and time the board was created.
}
