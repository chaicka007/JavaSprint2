package managers;

import entities.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> watchHistory = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (watchHistory.size() < MAX_HISTORY_SIZE) {
            watchHistory.add(task);
        } else {
            watchHistory.removeFirst();
            watchHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(watchHistory);
    }
}
