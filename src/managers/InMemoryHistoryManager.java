package managers;

import entities.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> watchHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (watchHistory.size() < 10) {
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
