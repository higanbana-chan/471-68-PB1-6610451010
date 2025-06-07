package cs211.project.models.collections;

import cs211.project.models.Role;
import cs211.project.models.Team;

import java.util.ArrayList;
import java.util.List;

public class RoleCollection {
    private List<Role> roles;

    public RoleCollection() {
        roles = new ArrayList<>();
    }

    public void addRole(String username, String eventId, String role) {
        username = username.trim();
        eventId = eventId.trim();
        role = role.trim();
        roles.add(new Role(username, eventId, role));
    }

    public Role findUserByEventIdAndUsername(String username , String eventId){
        for (Role role : roles){
            if (  role.getUsername().equals(username) && role.getEventId().equals(eventId)  ){
                return role;
            }
        }
        return null;
    }

    public Role findUserByUsername(String username){
        for (Role role : roles){
            if (  role.getUsername().equals(username)  ) {
                return role;
            }
        }
        return null;
    }
    public boolean findLeaderInTeam(String teamId){
        for (Role role1 : roles){
            if (role1.getEventId().equals(teamId)){
                if (role1.getRole().equals("Leader")){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUserJoinTeam(String username, String eventId, TeamList teamList) {
        for (Role role : roles) {
            if (role.getUsername().equals(username)) {
                Team team = teamList.findTeamByTeamId(role.getEventId());
                if (team != null) {
                    if (team.getEventId().equals(eventId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isUserTeamBan(String username, String eventId, TeamList teamList) {
        for (Role role : roles) {
            if (role.getUsername().equals(username)) {
                Team team = teamList.findTeamByTeamId(role.getEventId());
                if (team != null) {
                    if (team.getEventId().equals(eventId)) {
                        if (role.getRole().equals("BAN"))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void setRoleByEventIdAndUsername(String username, String eventId , String role){
        Role exist = findUserByEventIdAndUsername(username,eventId);
        if(exist != null){
            exist.setRole(role);
        }
    }
    public boolean findUserBan(String username , String eventId){
        Role role = findUserByEventIdAndUsername(username,eventId);
        if (role != null && role.getRole().equals("BAN")){
            return true;
        }
        return false;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
