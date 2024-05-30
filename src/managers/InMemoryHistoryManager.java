package managers;

import entities.Task;
import java.util.List;



public class InMemoryHistoryManager implements HistoryManager {
    private HistoryList history = new HistoryList();

    @Override
    public void add(Task task) {
        history.add(task);
    }


    @Override
    public void remove(long id) {
        history.removeById(id);
    }


    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }


}



