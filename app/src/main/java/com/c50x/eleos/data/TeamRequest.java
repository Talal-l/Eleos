package com.c50x.eleos.data;


public class TeamRequest extends Request {

    private String teamName;
    private String teamAdmin;


    public TeamRequest(Team team, String receiver, int state) {
        this.setReceiver(receiver);
        this.setSender(team.getTeamAdmin());
        this.setState(state);
        this.teamName = team.getTeamName();
        this.teamAdmin = team.getTeamAdmin();
        this.setTitle("Team Invitation");
        this.setType("TeamRequest");
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(String teamAdmin) {
        this.teamAdmin = teamAdmin;
    }
}
