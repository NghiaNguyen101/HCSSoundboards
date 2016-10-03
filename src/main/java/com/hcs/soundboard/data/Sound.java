package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class Sound {
    private int id;
    private String name;
    private InputStream sound;
    private int size;
}
