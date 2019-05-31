import java.util.Comparator;

class TimeComparator
        implements Comparator<Event> {
    public int compare(Event event, Event event1) {
        return event.getStartTime().compareTo(event1.getStartTime());
    }
}
