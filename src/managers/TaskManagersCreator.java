package managers;

public class TaskManagersCreator {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
