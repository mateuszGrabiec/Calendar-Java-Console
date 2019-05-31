public interface PriorityGetter {
      public default Prefs getPriority(){
          return Prefs.None;
      }
    }
