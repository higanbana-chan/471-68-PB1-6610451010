package cs211.project.models.collections;

import cs211.project.models.Event;
import cs211.project.models.Role;
import cs211.project.models.UserAccount;

import java.util.ArrayList;
import java.util.Comparator;

public class EventCollection {
    private ArrayList<Event> events;

    public EventCollection() {
        events = new ArrayList<>();
    }

    public void addEvent(String id, String name, String poster, String detail, String start, String end, String type, String location, String organizer, int currParticipants, int maxParticipants) {
        events.add(new Event(id, name, poster, detail, start, end, type, location, organizer, currParticipants, maxParticipants));
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void addUserToEvent(AccountCollection accountCollection, RoleCollection roleCollection) {
        for (Event event : events) {
            for (Role role : roleCollection.getRoles()) {
                if (role.getEventId().equals(event.getId()) && role.getRole().equals("Participant")) {
                    UserAccount userAccount = accountCollection.findAccount(role.getUsername());
                    event.getParticipantList().addAccount(userAccount);
                }
            }
        }
    }

    public void setCurrentParticipant(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                event.setCurrParticipants(true);
            }
        }
    }

    public Event findEventById(String eventId) {
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }


    public void sortEventByEndDate(ArrayList<Event> a, Comparator<Event> cmp) {
        for (int i = 0; i < a.size() - 1; i++) {// i แบ่งส่วนเรียงแล้วกับยังไม่เรียง
            int minPos = i;
            for (int j = i + 1; j < a.size(); j++) { // วนลูปหาค่าน้อยสุด
                if (cmp.compare(a.get(j), a.get(minPos)) > 0) {
                    minPos = j;
                }
            }
            // สลับข้อมูลใน minPos และ i ทําให้ข้อมูลใน minPos ไปอยู่ส่วนที่เรียงแล้ว
            Event temp = a.get(minPos);
            a.set(minPos, a.get(i));
            a.set(i, temp);
        }
    }

    public EventCollection findEventByName(String eventName) {
        EventCollection searchEvent = new EventCollection();
        for (Event event : events) {
            if (event.getName().toLowerCase().contains(eventName.toLowerCase())) {
                searchEvent.addEvent(event);
            }
        }
        return searchEvent;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
