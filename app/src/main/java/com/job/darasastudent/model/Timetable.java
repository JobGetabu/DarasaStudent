package com.job.darasastudent.model;

import java.util.Date;

/**
 * Created by Job on Wednesday : 11/21/2018.
 */
public class Timetable {

    //Populates students timetables

    private String lecid;
    private String lecteachid;
    private String lecteachtimeid;
    private String timetableid;
    private String day;
    private Date time;
    private String semester;
    private String currentyear;
    private String yearofstudy;
    private String course;

    public Timetable() {
    }

    public Timetable(String lecid, String lecteachid, String lecteachtimeid,
                     String timetableid, String day, Date time, String semester, String currentyear, String yearofstudy, String course) {
        this.lecid = lecid;
        this.lecteachid = lecteachid;
        this.lecteachtimeid = lecteachtimeid;
        this.timetableid = timetableid;
        this.day = day;
        this.time = time;
        this.semester = semester;
        this.currentyear = currentyear;
        this.yearofstudy = yearofstudy;
        this.course = course;
    }

    public String getLecid() {
        return lecid;
    }

    public void setLecid(String lecid) {
        this.lecid = lecid;
    }

    public String getLecteachid() {
        return lecteachid;
    }

    public void setLecteachid(String lecteachid) {
        this.lecteachid = lecteachid;
    }

    public String getLecteachtimeid() {
        return lecteachtimeid;
    }

    public void setLecteachtimeid(String lecteachtimeid) {
        this.lecteachtimeid = lecteachtimeid;
    }

    public String getTimetableid() {
        return timetableid;
    }

    public void setTimetableid(String timetableid) {
        this.timetableid = timetableid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCurrentyear() {
        return currentyear;
    }

    public void setCurrentyear(String currentyear) {
        this.currentyear = currentyear;
    }

    public String getYearofstudy() {
        return yearofstudy;
    }

    public void setYearofstudy(String yearofstudy) {
        this.yearofstudy = yearofstudy;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "lecid='" + lecid + '\'' +
                ", lecteachid='" + lecteachid + '\'' +
                ", lecteachtimeid='" + lecteachtimeid + '\'' +
                ", timetableid='" + timetableid + '\'' +
                ", day='" + day + '\'' +
                ", time=" + time +
                ", semester='" + semester + '\'' +
                ", currentyear='" + currentyear + '\'' +
                ", yearofstudy='" + yearofstudy + '\'' +
                ", course='" + course + '\'' +
                '}';
    }


}
