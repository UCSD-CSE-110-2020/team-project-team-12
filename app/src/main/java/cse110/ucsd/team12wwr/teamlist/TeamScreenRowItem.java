package cse110.ucsd.team12wwr.teamlist;

public class TeamScreenRowItem {

    private String memberName;
    private String initials;
    private String teamID;

    public TeamScreenRowItem(String name, String init, String teamID) {
        this.memberName = name;
        this.initials = init;
        this.teamID = teamID;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getInitials() {
        return this.initials;
    }

    public String getTeamID() {
        return this.teamID;
    }
}
