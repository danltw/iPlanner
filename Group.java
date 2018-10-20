package com.project42.iplanner.Groups;

import com.project42.iplanner.Accounts.Account;

import java.util.ArrayList;

public class Group {

    private int groupID;

    private String groupName;

    private ArrayList<Account> members;

    private ArrayList<Account> admins;

    public Group() {

    }

    public Group(int groupID, String groupName, ArrayList<Account> members, ArrayList<Account> admins) {
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

    public ArrayList<Account> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Account> members) {
        this.members = members;
    }

    public ArrayList<Account> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Account> admins) {
        this.admins = admins;
    }
}
