package com.project42.iplanner.Reviews;

import java.sql.Time;
import java.util.Date;

public class Review {

    public Review() {

    }

    public Review(int commentID, int commenterID, Date cDate, Time cTime, String commentTxt) {
        this.commentID = commentID;
        this.commenterID = commenterID;
        this.cDate = cDate;
        this.cTime = cTime;
        this.commentTxt = commentTxt;
    }

    private int commentID;

    private int commenterID;

    private Date cDate;

    private Time cTime;

    private String commentTxt;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getCommenterID() {
        return commenterID;
    }

    public void setCommenterID(int commenterID) {
        this.commenterID = commenterID;
    }

    public Date getcDate() {
        return cDate;
    }

    public void setcDate(Date cDate) {
        this.cDate = cDate;
    }

    public Time getcTime() {
        return cTime;
    }

    public void setcTime(Time cTime) {
        this.cTime = cTime;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

}
