package com.c50x.eleos.data;


public class TeamRequest extends Request {

    private String teamName;
    private String teamAdmin;


    public TeamRequest(Team team, String receiver, int state) {
        setReceiver(receiver);
        setSender(team.getTeamAdmin());
        setState(state);
        this.teamName = teamName;
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
