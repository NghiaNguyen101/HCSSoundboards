package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor
public class Board {
    int id;
    List<SoundMetadata> sounds;
    String ownerName;
    String boardName;
    String description;
    boolean isPublic;
    Date createDate;
}
