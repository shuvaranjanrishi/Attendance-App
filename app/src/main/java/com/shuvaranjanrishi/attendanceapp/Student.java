package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

public class Student {

    private long sid;
    private int roll;
    private String name,status;

    public Student(long sid,int roll, String name) {
        this.sid = sid;
        this.roll = roll;
        this.name = name;
        this.status = "";
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "roll='" + roll + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
