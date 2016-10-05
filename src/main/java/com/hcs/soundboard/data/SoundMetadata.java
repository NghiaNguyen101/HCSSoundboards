package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class SoundMetadata {
    private int id;
    private String name;
    private boolean isPublic;
}
