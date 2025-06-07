package cs211.project.services;

import cs211.project.models.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class EventDateComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedEndDate1 = o1.getEnd();
        String formattedEndDate2 = o2.getEnd();
        LocalDate endDate1 = LocalDate.parse(formattedEndDate1, dtf);
        LocalDate endDate2 = LocalDate.parse(formattedEndDate2, dtf);
        return endDate1.compareTo(endDate2);
    }
}
