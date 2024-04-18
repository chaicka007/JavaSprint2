package managers;

import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    Map<Long, Task> tasks = new HashMap<>();
    Map<Long, Epic> epics = new HashMap<>();
    Map<Long, Subtask> subtasks = new HashMap<>();
    long idCount = 0;

    private long generateId() {
        idCount++;
        return idCount;
    }

    public void newTask(Task newTask) { //Создание новой задачи
        for (Task task : tasks.values()) {
            if (newTask.equals(task)) {
                System.out.println("такая задача уже есть");
                return;
            }
        }
        newTask.setId(generateId());
        tasks.put(newTask.getId(), newTask);
    }

    public void newEpic(Epic newEpic) {
        for (Epic epic : epics.values()) {
            if (newEpic.equals(epic)) {
                System.out.println("такой эпик уже есть!");
                return;
            }
        }
        newEpic.setId(generateId());
        epics.put(newEpic.getId(), newEpic);
    }

    public void newSubtask(Subtask newSubtask) {
        if (!epics.containsKey(newSubtask.getEpicId())) return; //Проверка что есть эпик с таким id
        for (Subtask subtask : subtasks.values()) {
            if (newSubtask.equals(subtask)) {
                System.out.println("Такая подзадача уже есть!");
                return;
            }
        }
        newSubtask.setId(generateId());
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.get(newSubtask.getEpicId()).addSubtasksId(newSubtask.getId()); //Добавляем инфу о сабтасках в эпик
        updateEpicStatus(newSubtask.getEpicId());
    }

    public void updateTask(Task updateTask) { //Обновление задачи,
        // проверка, что id задачи существует и задача не повторяется
        if (tasks.containsKey(updateTask.getId()) && !updateTask.equals(tasks.get(updateTask.getId()))) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId()) && !updateEpic.equals(epics.get(updateEpic.getId()))) {
            epics.put(updateEpic.getId(), updateEpic);
        }
    }


    public void updateSubtask(Subtask updateSubtask) {
        if (subtasks.containsKey(updateSubtask.getId()) && !updateSubtask.equals(subtasks.get(updateSubtask.getId()))) {
            subtasks.put(updateSubtask.getId(), updateSubtask);
            updateEpicStatus(updateSubtask.getEpicId()); //Обновляем эпик при обновлении подзадачи
        }
    }

    public void deleteTaskById(long id) { //удаление задачи по id
        tasks.remove(id);
    }

    public void deleteEpicById(long id) {
        if (epics.containsKey(id)) {
            for (Long subtaskId : epics.get(id).getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public void deleteSubtaskById(long id) {
        if (subtasks.containsKey(id)) {
            long idEpic = subtasks.get(id).getEpicId();
            subtasks.remove(id);
            delEpicSubtaskId(idEpic, id);
            updateEpicStatus(idEpic);
        }
    }

    public Task getTaskById(long id) { //Получение задачи по id
        return tasks.get(id);
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(long id) {
        return subtasks.get(id);
    }

    public ArrayList getSubtaskByEpicId(long id) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Long subtaskId : epics.get(id).getSubtasksId()) {
            subtaskArrayList.add(subtasks.get(subtaskId));
        }
        return subtaskArrayList;
    }

    public void removeTasks() { //Удаление всех задач
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear(); //Удаление всех подзадач, тк эпиков больше нет
    }

    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            clearEpicSubtasksId(epic.getId());
        }
    }

    public Map<Long, Task> getTasks() { //Получение списка всех задач
        return new HashMap<>(tasks);
    }

    public Map<Long, Epic> getEpics() {
        return new HashMap<>(epics);
    }

    public Map<Long, Subtask> getSubtasks() {
        return new HashMap<>(subtasks);
    }

    private void updateEpicStatus(long id) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Long subtaskId : epics.get(id).getSubtasksId()) {
            statuses.add(subtasks.get(subtaskId).getStatus());
        }
        if (statuses.size() > 1) {
            for (int i = 1; i < statuses.size(); i++) { //Сравнение всех статусов подзадач
                if (statuses.get(i).equals(Status.NEW) && statuses.get(i - 1).equals(Status.NEW)) {
                    epics.get(id).setStatus(Status.NEW);
                } else if (statuses.get(i).equals(Status.DONE) && statuses.get(i - 1).equals(Status.DONE)) {
                    epics.get(id).setStatus(Status.DONE);
                } else {
                    epics.get(id).setStatus(Status.IN_PROGRESS);
                    break;
                }
            }
        } else if (statuses.size() == 1) {
            epics.get(id).setStatus(statuses.getFirst());
        } else {
            epics.get(id).setStatus(Status.NEW);
        }
    }

    private void clearEpicSubtasksId(long epicId) {
        epics.get(epicId).getSubtasksId().clear();
    }

    private void delEpicSubtaskId(long epicId, long id) {
        epics.get(epicId).getSubtasksId().remove(id);
    }
}