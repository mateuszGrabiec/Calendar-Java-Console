import javafx.scene.layout.Priority;

import java.time.LocalTime;
import java.util.prefs.Preferences;

public class Zadanie extends Event {
    private Prefs priority;

    public String toString(){
        String output;
        output=this.startTime.toString();
        output+=" "+this.endTime.toString();
        output+="\t"+Prefs.translate(this.status);
        output+="\t"+Prefs.translate(this.priority);
        output+=" "+this.content;
        return output;
    }

    public Prefs getPriority() {
        return this.priority;
    }

    public boolean setPriority(Prefs newPriority) {
        boolean isSet=false;
       try{
           this.priority=newPriority;
           isSet=true;
       }catch (IllegalArgumentException exception){//
       }
        return isSet;
    }

    public boolean setStatus(Prefs status){
        boolean ifSet=false;
        try {
        this.status=status;
        ifSet=true;
        }catch (IllegalArgumentException exception){//
        }
        return ifSet;
    }




    public Zadanie(LocalTime startTime,LocalTime endTime,String content, Prefs status, Prefs priority){
        this.endTime=endTime;
        this.startTime=startTime;
        this.content=content;
        this.status=status;
        this.priority=priority;
    }
}
