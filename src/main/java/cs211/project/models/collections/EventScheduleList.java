package cs211.project.models.collections;

import cs211.project.models.EventSchedule;

import java.util.ArrayList;

public class EventScheduleList {
    private ArrayList<EventSchedule> eventSchedules;

    public EventScheduleList() { eventSchedules = new ArrayList<>(); }

    public void addNewEventsDetail(String time,String nameAct, String detail,String eventId) {
        time = time.trim();
        nameAct = nameAct.trim();
        detail = detail.trim();
        eventId = eventId.trim();
        eventSchedules.add(new EventSchedule(time ,nameAct, detail, eventId));
    }

    public EventSchedule findEventDetailById(long idRecord) {
        for (EventSchedule eventSchedule : eventSchedules) {
            if (eventSchedule.isId(idRecord)) {
                return eventSchedule;
            }
        }
        return null;
    }


    public void setTimeById(long idRecord,String time){
        EventSchedule exist = findEventDetailById(idRecord);
    if (exist != null){
        exist.setTime(time);
        }
    }

    public void setDetailById(long idRecord,String detail){
        EventSchedule exist = findEventDetailById(idRecord);
        if (exist != null){
            exist.setDetail(detail);
        }
    }

    public void setNameById(long idRecord,String nameAct){
        EventSchedule exist = findEventDetailById(idRecord);
        if (exist != null){
            exist.setNameAct(nameAct);
        }
    }



    public ArrayList<EventSchedule> getEventDetails() {
        return eventSchedules;
    }
}
