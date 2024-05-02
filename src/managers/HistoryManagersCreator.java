package managers;

public class HistoryManagersCreator {
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
