package managers;

import entities.Epic;
import entities.Subtask;
import entities.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {


    public void newTask(Task newTask);

    public void newEpic(Epic newEpic);

    public void newSubtask(Subtask newSubtask);

    public void updateTask(Task updateTask);

    public void updateEpic(Epic updateEpic);


    public void updateSubtask(Subtask updateSubtask);

    public void deleteTaskById(long id);

    public void deleteEpicById(long id);

    public void deleteSubtaskById(long id);

    public Task getTask(long id);

    public Epic getEpic(long id);

    public Subtask getSubtask(long id);

    public ArrayList getSubtaskByEpicId(long id);

    public void removeTasks();

    public void removeEpics();

    public void removeSubtasks();

    public Map<Long, Task> getTasks();

    public Map<Long, Epic> getEpics();

    public Map<Long, Subtask> getSubtasks();
    public List<Task> getHistory();

}
