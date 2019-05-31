public enum Prefs {
        Low(1),
        Middle(2),
        High(3),
        Planned(4),
        Confirmed(5),
        Free(6),
        During(7),
        Done(8),
        None(0);

        private int prefs;

        public int getPrefs(){
            return this.prefs;
        }

        public static String translate(Prefs prefs){
            switch (prefs){
                case Low:
                    return "Niski       ";
                case Done:
                    return "Zrobione    ";
                case Free:
                    return "Wolne       ";
                case High:
                    return "Wysoki      ";
                case None:
                    return "";
                case During:
                    return "W toku      ";
                case Middle:
                    return "Średni      ";
                case Planned:
                    return "Zaplanowane ";
                case Confirmed:
                    return "Potwierdzone";
                    default:
                        return "";
            }
        }

        Prefs(int i) {
            this.prefs = i;
        }
    }
