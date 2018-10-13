package com.project42.iplanner.Bookmarks;

public class Bookmark {

    private int bookmarkID;

    private POI poi;

    public Bookmark() {
    }

    public Bookmark(int bookmarkID, POI poi) {
        this.bookmarkID = bookmarkID;
        this.poi = poi;
    }

    public int getBookmarkID() {
        return bookmarkID;
    }

    public void setBookmarkID(int bookmarkID) {
        this.bookmarkID = bookmarkID;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }
}
