package cs211.project.models;

public class EventSchedule {
    private String time ;
    private String nameAct;
    private String detail;
    private long idRecord;
    private static long nextId = 1;
    private String eventId ;

    public EventSchedule(String time,String nameAct, String detail,String eventId){
        this.time = time;
        this.nameAct = nameAct;
        this.detail = detail;
        this.idRecord = nextId;
        this.eventId = eventId;
        nextId++;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public void setNameAct(String nameAct) { this.nameAct = nameAct; }

    public boolean isId(long idRecord) {
        return this.idRecord == (idRecord);
    }

    public String getDetail() {
        return detail;
    }

    public String getNameAct() { return nameAct; }

    public String getTime() {
        return time;
    }

    public String getEventId() {
        return eventId;
    }

    public long getIdRecord() { return idRecord; }
}
