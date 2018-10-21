package com.sszg.studygroups.data;

public class Subject {
    private String professorName, courseName, roomNumber, time, profileURL;

    public Subject(String professorName, String className, String roomNumber, String time, String profileURL) {
        this.professorName = professorName;
        this.courseName = className;
        this.roomNumber = roomNumber;
        this.time = time;
        this.profileURL = profileURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
