import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    private static String isEdit(boolean edit){
        if(edit)return "Udało się edytować";
        return "Nie udało się edytować";
    }

    private static String showResultOfSaving(boolean isTaskSaved){
        String message;
        if(isTaskSaved){
            message= "Udało się dodać zdanie";
        }else{
            message = "Niepoprawne dane";
        }
        return message;
    }

    private static String showResultOfRemoving(boolean isRemoved){
        String message;
        if(isRemoved) message="Udało się usunąć zadanie";
        else message ="Niepoprawne dane";
        return message;
    }

    private static LocalTime pobierzCzas(boolean start){
        if(start)System.out.println("Podaj godzine rozpoczęcia");
        else System.out.println("Podaj godzine zakończenia");
        int h=24;
        int m=61;
        while (!(h<24 && h>-1 && m<61 && m>-1)) {
            Scanner scan = new Scanner(System.in);
            try{
            String godz = "";
            godz=scan.next();

                if (godz.contains(":")) {
                h = Integer.parseInt(godz.substring(0, godz.indexOf(":")));
                m = Integer.parseInt(godz.substring(godz.indexOf(":") + 1));
                }
            }catch (NumberFormatException ex){//
            }
            if(!(h<24 && h>-1 && m<61 && m>-1)) System.out.println("Podaj godzine jeszcze raz");
        }
        return LocalTime.of(h,m);
    }

    public static LocalTime[] sprawdzPoprawnoscCzasu(LocalTime czas1, LocalTime czas2){
        LocalTime []godziny=new LocalTime[2];

        godziny[0]=czas1;
        godziny[1]=czas2;
        while (!godziny[0].isBefore(godziny[1])){
            System.out.println("Zadanie musi się zacząć zanim się skończy, edytuj jeden z czasów\n1.Czas rozpoczęcia\n2.Czas.zakończenia");
            try {
                switch (pobierzLiczbe()){
                    case 1:
                        godziny[0]=pobierzCzas(true);
                        break;
                    case 2:
                        godziny[1]=pobierzCzas(false);
                        break;
                }
            }catch (java.util.InputMismatchException e){
                System.out.println("Wpisano złe dane");
            }

        }
        return godziny;
    }

    public static void wasTaskInList(boolean wasTask){
        if(!wasTask) System.out.println("Nie znalezione wydarzeń w tym dniu");
    }

    public static void wyswietlanie(Week tydzien){
        System.out.println("Podaj dzień");
        int day=pobierzLiczbe();
        if(day<=7 && day>=0) {
            System.out.println("Co chcesz wyświetlić\n1.Wszystkie zdarzenia\n2.Wszystkie zadania\n3.Zadania ze statusem i priorytetem\n4.Wszystkie spotkania\n5.Spotkania ze statusem");
            Prefs status, priorytet;
            boolean wasTask;
            switch (pobierzLiczbe()) {
                case 1:
                    wasTask=tydzien.printList(day, ev -> System.out.println(ev));
                    wasTaskInList(wasTask);
                    break;
                case 2:
                    wasTask=tydzien.printList(day, ev -> {
                        if(ev instanceof Zadanie)System.out.println(ev);
                    });
                    wasTaskInList(wasTask);
                    break;
                case 3:
                    status=wybierzStatus(true);
                    priorytet=wybierzPriorytet();
                    wasTask=tydzien.printList(day, ev -> {
                        if(ev instanceof Zadanie && ev.getStatus().equals(status) && ev.getPriority().equals(priorytet)) System.out.println(ev);
                    });
                    wasTaskInList(wasTask);
                    break;
                case 4:
                    wasTask=tydzien.printList(day, ev -> {
                        if(ev instanceof Meeting)System.out.println(ev);
                    });
                    wasTaskInList(wasTask);
                    break;
                case 5:
                    status=wybierzStatus(false);
                    wasTask=tydzien.printList(day, ev -> {
                        if(ev instanceof Meeting && ev.getStatus().equals(status))System.out.println(ev);
                    });
                    wasTaskInList(wasTask);
                    break;
                default:
                    System.out.println("Brak takiej opcji");
            }
        }else System.out.println("Podano zły dzień");
    }

    public static void edytujZdarzenie(Week tydzien, int day){
        if(day<8 && day>0) {
            System.out.println("Podaj godzine zadania, które chcesz edytować");
            LocalTime oldTime=pobierzCzas(true);
            if(tydzien.getEvent(day, oldTime) != null) {
                System.out.println("Co chcesz edytować\n1.Godzina rozpoczęcia\n2.Godzina zakończenia\n3.Treść\n4.edycja statusu\n5.Edycja priorytetu");
                LocalTime startTime,endTime;
                int choice = pobierzLiczbe();
                if(choice<=5 && choice>=1)System.out.println("Wprowadź nowe dane");
                switch (choice) {
                    case 1: case 2:
                            if (choice == 1) {
                                startTime = pobierzCzas(true);
                                while (tydzien.getEvent(day, startTime) != null) {
                                    System.out.println("Jest już zadanie o tej godzinie, wpisz inną godzinę");
                                    startTime = pobierzCzas(true);
                                }
                                endTime = tydzien.getEvent(day, oldTime).getEndTime();
                            } else {
                                endTime = pobierzCzas(false);
                                startTime = tydzien.getEvent(day, oldTime).getStartTime();
                            }
                            LocalTime[] godziny = sprawdzPoprawnoscCzasu(startTime, endTime);
                            boolean isEdited=tydzien.editEvent(oldTime,godziny[0], godziny[1], day);
                            System.out.println(isEdit(isEdited));
                        break;
                    case 3:
                        System.out.println(isEdit(tydzien.editEvent(day,oldTime,pobierzTekst())));
                        break;
                    case 4:
                        edytujStatus(tydzien.getEvent(day,oldTime));
                        break;
                    case 5:
                        edytujPriorytet(tydzien.getEvent(day,oldTime));
                    default:
                        System.out.println("Wbrałeś złą opcje");
                }
            } else System.out.println("Nie ma wydarzenia o tej godzinie");
        }
    }

    public static void edytujStatus(Event event){
        boolean isSet=true;
        if (event instanceof Zadanie){
            System.out.println("Wybierz nowy status\n1.Wolne\n2.W trakcie\n3.Zrobione");
            switch (pobierzLiczbe()){
                case 1://wolne
                    event.setStatus(Prefs.Free);
                    break;
                case 2://w toku
                    event.setStatus(Prefs.During);
                    break;
                case 3:// wykonane
                    event.setStatus(Prefs.Done);
                    break;
                    default:
                        isSet=false;
            }
        }else if (event instanceof Meeting){
            System.out.println("Wybierz nowy status\n1.Zaplanowane\n2.Potwierdzone");
            switch (pobierzLiczbe()) {
                case 1://zaplanowane
                    event.setStatus(Prefs.Planned);
                    break;
                case 2://potwierdzone
                    event.setStatus(Prefs.Confirmed);
                    break;
                default:
                    isSet=false;
            }
            }
        System.out.println(isEdit(isSet));
    }

    public static void edytujPriorytet(Event event){
        if (event instanceof Zadanie){
            System.out.println("Wybierz nowy priorytet\n1.Niski\n2.Średni\n3.Wysoki");
            switch (pobierzLiczbe()) {
                case 1://niski
                    ((Zadanie) event).setPriority(Prefs.Low);
                    System.out.println(isEdit(true));
                    break;
                case 2://sredni
                    ((Zadanie) event).setPriority(Prefs.Middle);
                    System.out.println(isEdit(true));
                    break;
                case 3://wysoki
                    ((Zadanie) event).setPriority(Prefs.High);
                    System.out.println(isEdit(true));
                    break;
                default:
                    System.out.println("Wybrałeś zła opcje");
            }
        }else System.out.println("Brak zadania o tej godzinie");
    }

    public static int pobierzLiczbe(){
        try {
            Scanner pobierz = new Scanner(System.in);
            return pobierz.nextInt();
        }catch (java.util.InputMismatchException e){
          return -1;
        }
    }

    public static String pobierzTekst(){
        try {
            Scanner pobierz = new Scanner(System.in);
            System.out.println("Podaj treść");
            return pobierz.nextLine();
        }catch (java.util.InputMismatchException e){
            return "";
        }
    }
    public static void dodajWydarzenie(Week tydzien){
        int statusIPriorytet;
        Prefs status=Prefs.None;
        Prefs priority=Prefs.None;
        System.out.println("Podaj dzień");
        int day=pobierzLiczbe();
        if(day<8 && day>0) {
            LocalTime startTime = pobierzCzas(true);
            LocalTime endTime = pobierzCzas(false);
            System.out.println("Podaj typ wydarzenia\n1.Zadanie\n2.Spotkanie");
            int typ = 0;
            while (typ != 1 && typ != 2) {
                typ = pobierzLiczbe();
                if (typ == 1) {
                    status = wybierzStatus(true);
                    priority = wybierzPriorytet();
                    if (status != Prefs.None && priority != Prefs.None && tydzien.getEvent(day,startTime)==null)
                        System.out.println(showResultOfSaving(tydzien.addObj(1, startTime, endTime, pobierzTekst(), status, priority)));
                    else System.out.println("Niepoprawne dane");
                } else if (typ == 2) {
                    status = wybierzStatus(false);
                    if (status != Prefs.None && tydzien.getEvent(day,startTime)==null)
                        System.out.println(showResultOfSaving(tydzien.addObj(1, startTime, endTime, pobierzTekst(), status, priority)));
                    else System.out.println("Niepoprawne dane");
                }
            }
        }else System.out.println("Wybrałeś nieprawidłowy dzień");
    }

    public static Prefs wybierzStatus(boolean isTask){
        if(isTask){
            System.out.println("Wybierz status\n1.Wolne\n2.W trakcie\n3.Zrobione");
            switch (pobierzLiczbe()){
                case 1://wolne
                    return Prefs.Free;
                case 2://w toku
                    return Prefs.During;
                case 3:// wykonane
                    return Prefs.Done;
                default:
                    return Prefs.None;
            }
        }else {
            System.out.println("Wybierz nowy status\n1.Zaplanowane\n2.Potwierdzone");
            switch (pobierzLiczbe()) {
                case 1://zaplanowane
                    return Prefs.Planned;
                case 2://potwierdzone
                    return Prefs.Confirmed;
                default:
                    return Prefs.None;
            }
        }
    }
    public static Prefs wybierzPriorytet(){
        System.out.println("Wybierz nowy priorytet\n1.Niski\n2.Średni\n3.Wysoki");
        switch (pobierzLiczbe()) {
            case 1://niski
                return Prefs.Low;
            case 2://sredni
                return Prefs.Middle;
            case 3://wysoki
                return Prefs.High;
            default:
                return Prefs.None;
        }
    }

    public static void main(String[] args) {
        Week tydzien = new Week();
        LocalTime czas1=LocalTime.of(1,0);
        LocalTime czas11=LocalTime.of(1,30);
        LocalTime czas2=LocalTime.of(2,0);
        LocalTime czas3=LocalTime.of(3,0);
        LocalTime czas4=LocalTime.of(4,0);
        LocalTime czas5=LocalTime.of(5,0);
        tydzien.addObj(1,czas1,czas2,"zad2",Prefs.Done,Prefs.Low);
        tydzien.addObj(1,czas2,czas3,"zad1",Prefs.Free,Prefs.Middle);
        tydzien.addObj(1,czas3,czas4,"met1",Prefs.Planned,Prefs.None);
        tydzien.addObj(1,czas4,czas5,"met2",Prefs.Confirmed,Prefs.None);
        int wybor=0;
        do{
            System.out.println("MENU:\n1.Dodaj wydarzenie\n2.Usuń wydarzenie\n3.Pokaż wydarzenia\n4.Edytuj.wydarzenie\n5.Zakończ program");
            wybor=pobierzLiczbe();
            switch (wybor){
                case 1:
                    dodajWydarzenie(tydzien);
                    break;
                case 2:
                    System.out.println("Podaj dzień");
                    int dzienUsuniecia=pobierzLiczbe();
                    LocalTime czasUsuniecia=pobierzCzas(true);
                    System.out.println(showResultOfRemoving(tydzien.removeEvent(dzienUsuniecia,czasUsuniecia)));
                    break;
                case 3:
                    wyswietlanie(tydzien);
                    break;
                case 4:
                    System.out.println("Podaj dzień");
                    int dzienEdycji=pobierzLiczbe();
                    edytujZdarzenie(tydzien,dzienEdycji);
                    break;
                case 5:
                    break;
                    default:
                        System.out.println("Zły wybór");
            }
        }while (wybor!=5);
    }
}
