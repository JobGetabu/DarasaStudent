package com.job.darasastudent.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Job on Saturday : 10/13/2018.
 */


@Keep
@Entity(indices = {@Index("darasa"),
        @Index(value = {"teach_time", "class_time", "date_now"})})
public class ClassScan {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "teach_time")
    private String lecteachtimeid;
    @NonNull
    @ColumnInfo(name = "class_time")
    private Date classtime;
    @NonNull
    @ColumnInfo(name = "date_now")
    private Date date;

    public ClassScan(String lecteachtimeid, Date classtime, Date date) {
        this.lecteachtimeid = lecteachtimeid;
        this.classtime = classtime;
        this.date = date;
    }
}
