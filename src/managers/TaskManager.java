package managers;

import entities.Epic;
import entities.Subtask;
import entities.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {


    void newTask(Task newTask);

    void newEpic(Epic newEpic);

    void newSubtask(Subtask newSubtask);

    void updateTask(Task updateTask);

    void updateEpic(Epic updateEpic);


    void updateSubtask(Subtask updateSubtask);

    void deleteTaskById(long id);

    void deleteEpicById(long id);

    void deleteSubtaskById(long id);

    Task getTask(long id);

    Epic getEpic(long id);

    Subtask getSubtask(long id);

    ArrayList<Subtask> getSubtaskByEpicId(long id);

    void removeTasks();

    void removeEpics();

    void removeSubtasks();

    Map<Long, Task> getTasks();

    Map<Long, Epic> getEpics();

    Map<Long, Subtask> getSubtasks();
    List<Task> getHistory();

}
