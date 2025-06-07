package cs211.project.models;

public class TeamSchedule {
    private String nameAct_teamSchedule ;
    private String detail;
    private String status;
    private String idRecord;
    public static long nextTeamScheduleId;
    private String eventId ;
    private String teamId;

    public TeamSchedule(String idRecord, String nameAct_teamSchedule, String detail, String status, String eventId, String teamId) {
        this.nameAct_teamSchedule = nameAct_teamSchedule;
        this.detail = detail;
        this.status = status;
        this.idRecord = idRecord;
        this.eventId = eventId;
        this.teamId = teamId;
        nextTeamScheduleId++;
    }

    public String getNameAct_teamSchedule() {return nameAct_teamSchedule;}

    public String getStatus() {return status;}

    public String getDetail() {return detail;}

    public String getIdRecord() {return idRecord;}

    public String getEventId() {return eventId;}

    public String getTeamId() {return teamId;}

    public void setNameAct_teamSchedule(String nameAct_teamSchedule) {this.nameAct_teamSchedule = nameAct_teamSchedule;}

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setStatus(String status) {this.status = status;}
}
