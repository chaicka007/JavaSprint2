package managers;

import entities.Epic;
import entities.Subtask;
import entities.Task;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private final Path path;

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

    private void save(){

    }

    private
}
