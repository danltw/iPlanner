package com.project42.iplanner.Bookmarks;

import com.project42.iplanner.POIs.POI;

/** Represents a bookmark.
 * @author Team42
 * @version 1.0
 */
public class Bookmark {

    private int bookmarkID;

    private POI poi;

    /** Creates a bookmark with the specified bookmark ID and the corresponding
     * stored Place of Interest.
     * @param bookmarkID The bookmark ID assigned.
     * @param poi The Place of Interest that was bookmarked by the user.
     */
    public Bookmark(int bookmarkID, POI poi) {
        this.bookmarkID = bookmarkID;
        this.poi = poi;
    }

    /**
     * Get the current bookmark's bookmarkID property.
     * The bookmarkID property is essential to keep track of every bookmark stored by the user.
     * @return The current bookmark's bookmarkID.
     */
    public int getBookmarkID() {
        return bookmarkID;
    }

    /**
     * Set the current bookmark's bookmarkID property.
     * The bookmarkID property is essential to keep track of every bookmark stored by the user.
     * @param bookmarkID The bookmarkID assigned to the bookmark.
     */
    public void setBookmarkID(int bookmarkID) {
        this.bookmarkID = bookmarkID;
    }

    /**
     * Get the current Place of Interest associated with the bookmark.
     * The Place of Interest property is essential to keep track of the location bookmarked by the user.
     * @return The current bookmark's Place of Interest property.
     */
    public POI getPoi() {
        return poi;
    }

    /**
     * Set the current Place of Interest associated with the bookmark.
     * The Place of Interest property is essential to keep track of the location bookmarked by the user.
     * @param poi The Place of Interest associated with the bookmark.
     */
    public void setPoi(POI poi) {
        this.poi = poi;
    }
}
