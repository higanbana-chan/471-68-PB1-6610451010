package cs211.project.models.collections;
import cs211.project.models.EventSchedule;
import cs211.project.models.Role;
import cs211.project.models.Team;
import cs211.project.models.UserAccount;

import java.util.ArrayList;

public class TeamList {
    private ArrayList<Team> teams;

    public TeamList() { teams = new ArrayList<>(); }

    public void addNewTeam(String teamID,String teamName,int capacity,String eventId,String start ,String end) {
        teamName = teamName.trim();
        teams.add(new Team(teamID,teamName,capacity,eventId,start,end));
    }
    public void addNewTeam(Team team) {
        teams.add(team);
    }

    public void addUserToTeam(AccountCollection accountCollection, RoleCollection roleCollection) {
        for (Role role : roleCollection.getRoles()) {
            for (Team team : teams) {
                if (role.getEventId().equals(team.getTeamId()) && !role.getRole().equals("BAN")) {
                    UserAccount userAccount = accountCollection.findAccount(role.getUsername());
                    team.getMembers().addAccount(userAccount);
                }
            }
        }
    }

    public Team findTeamByTeamId(String teamId) {
        for (Team team : teams) {
            if (team.getTeamId().equals(teamId)) {
                return team;
            }
        }
        return null;
    }

    public Team findTeamByTeamName(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public void setTeamNameById(String idRecord,String teamName){
        Team exist = findTeamByTeamId(idRecord);
        if (exist != null){
            exist.setTeamName(teamName);
        }
    }

    public void setCapacityById(String idRecord,int capacity){
        Team exist = findTeamByTeamId(idRecord);
        if (exist != null){
            exist.setCapacity(capacity);
        }
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }
}
