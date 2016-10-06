package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

/**
 * Holds the actual sound clip.
 */
@Data
@AllArgsConstructor
public class SoundFile {
    private int id;
    private InputStream sound;
    private long size;
}
