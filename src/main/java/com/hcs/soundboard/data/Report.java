package com.hcs.soundboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
/**
 * Created by nnguy101 on 11/20/16.
 */
@Data @AllArgsConstructor
public class Report {
    private int reportId;
    private int boardId;
    private String boardTitle;
    private String reportUser;
    private String boardOwner;
    private String reportTitle;
    private String reportDesc;
    private Date reportDate;
    private String notes;
}
