package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

public class ClassItem {

    private long cid;
    private String className, subjectName;

    public ClassItem(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }

    public ClassItem(long cid, String className, String subjectName) {
        this.cid = cid;
        this.className = className;
        this.subjectName = subjectName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "ClassItem{" +
                "cid=" + cid +
                ", className='" + className + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
