package com.bignerdranch.android.sunflower.entity;

import java.io.Serializable;

public class Course implements Serializable {

    public int cId;
    public int weekStart;
    public int weekEnd;
    public int weekType;
    public int week;
    public int lessonStart;
    public int lessonEnd;
    public String username;
    public String cName;
    public String teacher;
    public String place;

    public Course(){

    }

    public Course(int cId,String username,int weekStart,int weekEnd,int weekType,int week,
                  int lessonStart,int lessonEnd,String cName,String teacher,String place){
        this.cId=cId;
        this.username=username;
        this.weekStart=weekStart;
        this.weekEnd=weekEnd;
        this.weekType=weekType;
        this.week=week;
        this.lessonStart=lessonStart;
        this.lessonEnd=lessonEnd;
        this.cName=cName;
        this.teacher=teacher;
        this.place=place;
    }

    public Course(String username,int weekStart,int weekEnd,int weekType,int week,
                  int lessonStart,int lessonEnd,String cName,String teacher,String place){
//        this.cId=cId;
        this.username=username;
        this.weekStart=weekStart;
        this.weekEnd=weekEnd;
        this.weekType=weekType;
        this.week=week;
        this.lessonStart=lessonStart;
        this.lessonEnd=lessonEnd;
        this.cName=cName;
        this.teacher=teacher;
        this.place=place;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(int weekStart) {
        this.weekStart = weekStart;
    }

    public int getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(int weekEnd) {
        this.weekEnd = weekEnd;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getLessonStart() {
        return lessonStart;
    }

    public void setLessonStart(int lessonStart) {
        this.lessonStart = lessonStart;
    }

    public int getLessonEnd() {
        return lessonEnd;
    }

    public void setLessonEnd(int lessonEnd) {
        this.lessonEnd = lessonEnd;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
