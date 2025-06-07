package cs211.project.models;

import cs211.project.models.collections.AccountCollection;
import cs211.project.models.collections.EventScheduleList;
import cs211.project.models.collections.TeamList;
import cs211.project.models.collections.TeamScheduleCollection;

import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private String poster;
    private String detail;
    private String start;
    private String end;
    private String type;
    private String location;
    private String organizer;
    private int currParticipants;
    private int maxParticipants;
    private AccountCollection participantList;
    private EventScheduleList scheduleList;
    private TeamList teamList;
    public static UUID nextEventId;

    public Event(String id, String name, String poster, String detail, String start, String end, String type, String location, String organizer, int currParticipants, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.poster = poster;
        this.detail = detail;
        this.start = start;
        this.end = end;
        this.type = type;
        this.location = location;
        this.currParticipants = currParticipants;
        this.maxParticipants = maxParticipants;
        this.organizer = organizer;
        participantList = new AccountCollection();
        scheduleList = new EventScheduleList();
        teamList = new TeamList();
    }

    public boolean isOrganizer(String username) {
        return username.equals(organizer);
    }

    public boolean isUserJoin(String username) {
        UserAccount exist = participantList.findAccount(username);
        return exist != null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCurrParticipants() {
        return currParticipants;
    }

    public void setCurrParticipants(boolean bool) {
        if (bool == true) {
            this.currParticipants++;
        } else {
            this.currParticipants--;
        }
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public AccountCollection getParticipantList() {
        return participantList;
    }

    public EventScheduleList getScheduleList() {
        return scheduleList;
    }

    public TeamList getTeamList() {
        return teamList;
    }

    public String getOrganizer() {
        return organizer;
    }

    public static String getNextEventId() {
        UUID random = UUID.randomUUID();
        return String.valueOf(random);
    }
}
