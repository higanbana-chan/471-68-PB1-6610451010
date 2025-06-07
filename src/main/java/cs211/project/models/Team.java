package cs211.project.models;

import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.TeamScheduleCollection;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Team {
    private String teamName;
    private int capacity;
    private String start;
    private String end;
    private String teamId;
    public static long nextTeamId;
    private String eventId;
    private TeamScheduleCollection teamScheduleCollection;
    private AccountCollection members;




    public Team(String teamId,String teamName,int capacity,String eventId, String start , String end){
        this.teamId = teamId;
        this.teamName = teamName;
        this.capacity = capacity;
        this.eventId = eventId;
        this.start = start;
        this.end = end;
        teamScheduleCollection = new TeamScheduleCollection();
        members = new AccountCollection();
        nextTeamId += 1;
    }

    public boolean isUserJoin(String username) {
        for (UserAccount userAccount : members.getUserAccounts()) {
            if (userAccount.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getCapacity() {
        return capacity;
    }

    public Integer getTeamCapacity() {
        return capacity;
    }

    public String getEventId() {
        return eventId;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public boolean recruit(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        LocalDate currDate = LocalDate.now();
        if (currDate.isAfter(startDate)&&currDate.isBefore(endDate)){
            return true;
        } else return false;
    }

    public TeamScheduleCollection getTeamScheduleCollection() { return teamScheduleCollection; }

    public AccountCollection getMembers() {
        return members;
    }
}
