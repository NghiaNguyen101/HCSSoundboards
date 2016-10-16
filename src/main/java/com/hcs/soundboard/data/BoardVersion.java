package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * This class holds information about a single soundboard version (shared or not shared).
 */
@Data @AllArgsConstructor
public class BoardVersion {
    private int boardId;
    private List<SoundMetadata> sounds;
    private String title; // The title of the soundboard
    private String description; // The description of the soundboard
    private boolean shared; // Whether or not this is the shared version.
    private Date updateDate; // The date and time that this version of the board was last last changed.
}
