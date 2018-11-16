package com.project42.iplanner.Groups;

import com.project42.iplanner.Accounts.Account;

import java.util.ArrayList;

/** Represents a group.
 * @author Team42
 * @version 1.0
 */
public class Group {

    private int groupID;

    private String groupName;

    private ArrayList<Integer> memberIDs;

    private ArrayList<Integer> adminIDs;

    /**
     * Creates an empty group with no property initialized
     */
    public Group() {

    }

    /**
     * Creates a group with the specified group ID, group name, userIDs of members and userIDs of admins.
     *
     * @param groupID   The group ID assigned.
     * @param groupName The name of the group as entered by the admins.
     * @param members   The userIDs of the members of the group.
     * @param adminIDs  The userIDs of the admins of the group.
     */
    public Group(int groupID, String groupName, ArrayList<Integer> members, ArrayList<Integer> adminIDs) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.memberIDs = memberIDs;
        this.adminIDs = adminIDs;
    }

    /**
     * Get the current group's groupID property.
     * The groupID property is essential to keep track of every group created.
     *
     * @return The current group's groupID.
     */
    public int getGroupID() {
        return groupID;
    }

    /**
     * Set the current group's groupID property.
     * The groupID property is essential to keep track of every group created.
     *
     * @param groupID The groupID assigned to the group.
     */
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    /**
     * Get the current group's groupName property.
     * The groupName property is essential to allow users to know what groups they are involved in.
     *
     * @return The current group's groupName.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Set the current group's groupName property.
     * The groupName property is essential to allow users to know what groups they are involved in.
     *
     * @param groupName The current group's groupName.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Get the current group's list of memberIDs property.
     * The memberIDs property is essential to keep track of the members in a group.
     *
     * @return The current group's list of member's IDs.
     */
    public ArrayList<Integer> getMemberIDs() {
        return memberIDs;
    }

    /**
     * Set the current group's memberIDs property.
     * TThe memberIDs property is essential to keep track of the members in a group.
     *
     * @param memberIDs memberIDs The current group's list of member's IDs.
     */
    public void setMemberIDs(ArrayList<Integer> memberIDs) {
        this.memberIDs = memberIDs;
    }

    /**
     * Get the current group's list of adminIDs property.
     * The adminIDs property is essential to keep track of the admins in a group.
     *
     * @return The current group's list of admin IDs.
     */
    public ArrayList<Integer> getAdminIDs() {
        return adminIDs;
    }

    /**
     * Set the current group's list of adminIDs property.
     * The adminIDs property is essential to keep track of the members in a group.
     *
     * @param adminIDs The current group's list of admin's IDs.
     */
    public void setAdmins(ArrayList<Integer> adminIDs) {
        this.adminIDs = adminIDs;
    }
}
