package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Holds metadata for a sound clip. The reason for having two different sound
 * classes is because we only need the metadata when we fetch the sounds for a
 * board to display the webpage. We need the boardId and name to create the button
 * and specify the URL at which to fetch the actual sound clip. We can't put
 * the sound clip itself directly on the webpage, only a reference to a URL
 * where you can get it.
 */
@Data @AllArgsConstructor
public class SoundMetadata {
    private int id;
    private String name;
    private String boxColor;
}
