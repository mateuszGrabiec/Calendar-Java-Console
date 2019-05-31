import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Week {
    private ArrayList<ArrayList<Event>> weekList;
    public boolean addObj(int day, LocalTime start, LocalTime end , String content, Prefs status, Prefs priority){
        boolean isAdd=false;
        day--;
        try {
            if(day<7 && day>=0 && start.isBefore(end)){
                if(weekList.get(day).size()==0){
                    ArrayList<Event> today =new ArrayList<>();
                    if(priority.getPrefs()>0){
                        Zadanie obj=new Zadanie(start,end,content,status,priority);
                        today.add(obj);
                    }
                    else{
                        Meeting obj = new Meeting(start,end,content,status);
                        today.add(obj);
                    }
                    today.sort(new TimeComparator());
                    weekList.add(day,today);
                    isAdd = true;
                }else {
                    if(findTask(day,start)==weekList.get(day).size()) {
                        ArrayList<Event> today = weekList.get(day);
                        if(priority.getPrefs()>0){
                            Zadanie obj=new Zadanie(start,end,content,status,priority);
                            today.add(obj);
                        }
                        else{
                            Meeting obj = new Meeting(start,end,content,status);
                            today.add(obj);
                        }
                        today.sort(new TimeComparator());
                        isAdd = true;
                    }
                }
            }
        } catch(IllegalArgumentException exception) {//wylapywanie bledu
        }

        return isAdd;
    }

    public boolean printList(int day, Consumer<Event> consumer) {
        day--;
        ArrayList<Event> list=this.weekList.get(day);
        boolean wasTask=false;
        if(list.size()!=0) {
                    for (Event event : list){
                        consumer.accept(event);
                        wasTask=true;
                    }
        }
        return wasTask;
    }


    public boolean editEvent(int day, LocalTime startTime, LocalTime newTime){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,startTime);
            int size= this.weekList.get(day).size();

            boolean isTaskOnThisSameHour=true;
            for (int i=0;i<size;i++){
                if(this.weekList.get(day).get(i).getStartTime().compareTo(newTime)==0) isTaskOnThisSameHour=false;
            }
            if(index < size && (isTaskOnThisSameHour || startTime.equals(newTime))) {
                if(newTime.isBefore(weekList.get(day).get(index).getEndTime())) {
                    if (this.weekList.get(day).get(index).setStartTime(newTime)) {
                        isEdited = true;
                        this.weekList.get(day).sort(new TimeComparator());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }

    public boolean editEvent(LocalTime startTime, LocalTime endTime, int day){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,startTime);
            int size= this.weekList.get(day).size();
            if(index < size ) {
                if(startTime.isBefore(endTime)) {
                    if (this.weekList.get(day).get(index).setEndTime(endTime)) {
                        isEdited = true;
                        this.weekList.get(day).sort(new TimeComparator());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }

    public boolean editEvent(LocalTime oldTime, LocalTime startTime, LocalTime endTime, int day){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,oldTime);
            int size= this.weekList.get(day).size();
            if(index < size ) {
                if (this.weekList.get(day).get(index).setStartTime(startTime) && this.weekList.get(day).get(index).setEndTime(endTime)) {
                    isEdited = true;
                    this.weekList.get(day).sort(new TimeComparator());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }

    /*public boolean editEvent(LocalTime startTime, int day, Prefs newStatus){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,startTime);
            if(index<this.weekList.get(day).size()) {
                if (this.weekList.get(day).get(index).setStatus(newStatus)) {
                    isEdited = true;
                    this.weekList.get(day).sort(new TimeComparator());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }

    public boolean editEvent(int day, Prefs newPriority,LocalTime startTime){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,startTime);
            if(index<this.weekList.get(day).size()) {
                if(this.weekList.get(day).get(index) instanceof Zadanie) {
                    if (((Zadanie) this.weekList.get(day).get(index)).setPriority(newPriority)) {
                        isEdited = true;
                        this.weekList.get(day).sort(new TimeComparator());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }*/

    public boolean editEvent(int day, LocalTime startTime, String newContent){
        day--;
        boolean isEdited=false;
        try{
            int index=this.findTask(day,startTime);
            if(index<this.weekList.get(day).size() && newContent.equals("")) {
                if (this.weekList.get(day).get(index).setContent(newContent)) {
                    isEdited = true;
                    this.weekList.get(day).sort(new TimeComparator());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {//catch
        }
        return isEdited;
    }

    public boolean removeEvent(int day, LocalTime startTime){
        boolean isRemoved=false;
        day--;
        try{
            int index = findTask(day, startTime);
            if(index<this.weekList.get(day).size()) {
                if (this.weekList.get(day).get(index) != null) this.weekList.get(day).remove(index);
                this.weekList.get(day).sort(new TimeComparator());
                isRemoved = true;
            }
        } catch (IndexOutOfBoundsException e) {//
        }
        return isRemoved;
    }

    public Event getEvent(int day,LocalTime startTime){
        day--;
        Event ev=null;
        int index= findTask(day,startTime);
        if (weekList.get(day).size()> index) ev=this.weekList.get(day).get(index);
        return ev;
    }

    private int findTask(int day, LocalTime startTime) {
        int index=0;
        while (index < this.weekList.get(day).size()) {
            if (this.weekList.get(day).get(index).getStartTime().equals(startTime)) {
                return index;
            }
            index++;
        }
        return index;
    }

    private int findTask(int day, String content) {
        int index=0;
        while (index < this.weekList.get(day).size()) {
            if (this.weekList.get(day).get(index).getContent().equals(content)) {
                return index;
            }
            index++;
        }
        return index;
    }


    public Week(){
        ArrayList<ArrayList<Event>> week =new ArrayList<ArrayList<Event>>();
        ArrayList<Event> day =new ArrayList<Event>();
        for (int i=0;i<=6;i++)week.add(i,day);
        this.weekList=week;
    }
}
