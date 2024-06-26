package managers;

import entities.Epic;
import entities.Subtask;
import entities.Task;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import entities.Type;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private  Path path = null;

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }


    @Override
    public void newTask(Task newTask) {
        super.newTask(newTask);
    }

    @Override
    public void newEpic(Epic newEpic) {
        super.newEpic(newEpic);
    }

    @Override
    public void newSubtask(Subtask newSubtask) {
        super.newSubtask(newSubtask);
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
    }

    @Override
    public void deleteTaskById(long id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteEpicById(long id) {
        super.deleteEpicById(id);
    }

    @Override
    public void deleteSubtaskById(long id) {
        super.deleteSubtaskById(id);
    }

    @Override
    public Task getTask(long id) {
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(long id) {
        return super.getEpic(id);
    }

    @Override
    public Subtask getSubtask(long id) {
        return super.getSubtask(id);
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(long id) {
        return super.getSubtaskByEpicId(id);
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
    }

    @Override
    public Map<Long, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public Map<Long, Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public Map<Long, Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public void save() {
        for (Subtask subtask : getSubtasks().values()){
            System.out.println(toString(subtask));
        }
        for (Task task : getTasks().values()){
            System.out.println(toString(task));
        }
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task instanceof Subtask) {
            sb.append(task.getId()).append(",").append(Type.SUBTASK).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription()).append(",")
                    .append(((Subtask) task).getEpicId());
            return sb.toString();
        } else if (task instanceof Epic) {
            sb.append(task.getId()).append(",").append(Type.EPIC).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription());
        } else if (task != null) {
            sb.append(task.getId()).append(",").append(Type.TASK).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription());
            return sb.toString();
        }
        return "error";
    }
}
