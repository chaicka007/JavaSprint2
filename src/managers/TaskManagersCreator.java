package managers;

public class TaskManagersCreator {
    public static TaskManager getDefault() {
        return new HttpTaskManager("localhost:8078", "save.csv");
    }
}
