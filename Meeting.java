import java.time.LocalTime;
import java.util.prefs.Preferences;

public class Meeting extends Event{
    public String toString(){
        String output;
        output=this.startTime.toString();
        output+=" "+this.endTime.toString();
        output+="\t"+Prefs.translate(this.status);
        output+="\t             "+this.content;
        return output;
    }



    public Meeting(LocalTime startTime, LocalTime endTime, String content, Prefs preference){
        this.endTime=endTime;
        this.startTime=startTime;
        this.content=content;
        this.status=preference;
    }

}
