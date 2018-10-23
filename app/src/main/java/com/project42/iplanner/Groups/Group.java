package com.project42.iplanner.Groups;

import com.project42.iplanner.Accounts.Account;

import java.util.ArrayList;

public class Group {

    private int groupID;

    private String groupName;

    private ArrayList<Integer> members;

    private ArrayList<Integer> admins;

    public Group() {

    }

    public Group(int groupID, String groupName, ArrayList<Integer> members, ArrayList<Integer> admins) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.members = members;
        this.admins = admins;
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

    public ArrayList<Integer> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }

    public ArrayList<Integer> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Integer> admins) {
        this.admins = admins;
    }
}
