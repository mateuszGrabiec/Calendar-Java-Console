import java.time.LocalTime;

public abstract class Event implements PriorityGetter{
protected LocalTime startTime;
protected LocalTime endTime;
protected String content;
protected Prefs status;

//getters
    public LocalTime getStartTime(){
        return this.startTime;
    }
    public LocalTime getEndTime(){ return this.endTime; }
    public String getContent() {
        return content;
    }
    public Prefs getStatus() { return status; }

//setters

    public boolean setStartTime(LocalTime startTime) {
        boolean ifSet=false;
        try {
            this.startTime=startTime;
            ifSet=true;
        }catch (IllegalArgumentException exception){//
        }
        return ifSet;
    }

    public boolean setEndTime(LocalTime endTime) {
        boolean ifSet=false;
        try {
            this.endTime=endTime;
            ifSet=true;
        }catch (IllegalArgumentException exception){//
        }
        return ifSet;
    }

    public boolean setContent(String content) {
        boolean ifSet=false;
        try {
            this.content=content;
            ifSet=true;
        }catch (IllegalArgumentException exception){//
        }
        return ifSet;
    }

    public boolean setStatus(Prefs newStatus) {
        boolean ifSet=false;
        try {
            this.status=newStatus;
            ifSet=true;
        }catch (IllegalArgumentException exception){//
        }
        return ifSet;
    }
}
