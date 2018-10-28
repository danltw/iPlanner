package com.project42.iplanner.Groups;

import com.project42.iplanner.Accounts.Account;

import java.util.ArrayList;

public class Group {

    private int groupID;

    private String groupName;

    private ArrayList<Integer> memberIDs;

    private ArrayList<Integer> adminIDs;

    public Group() {

    }

    public Group(int groupID, String groupName, ArrayList<Integer> members, ArrayList<Integer> adminIDs) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.memberIDs = memberIDs;
        this.adminIDs = adminIDs;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Integer> getMemberIDs() {
        return memberIDs;
    }

    public void setMemberIDs(ArrayList<Integer> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public ArrayList<Integer> getAdminIDs() {
        return adminIDs;
    }

    public void setAdmins(ArrayList<Integer> adminIDs) {
        this.adminIDs = adminIDs;
    }
}
