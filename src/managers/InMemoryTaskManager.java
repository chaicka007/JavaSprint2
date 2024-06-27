package managers;

import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = HistoryManagersCreator.getDefaultHistory();
    private long idCount = 0;

    private long generateId() {
        idCount++;
        return idCount;
    }

    @Override
    public void newTask(Task newTask) { //Создание новой задачи
        for (Task task : tasks.values()) {
            if (newTask.equals(task)) {
                System.out.println("такая задача уже есть");
                return;
            }
        }
        if (newTask.getId() == 0){
            newTask.setId(generateId());
        }
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void newEpic(Epic newEpic) {
        for (Epic epic : epics.values()) {
            if (newEpic.equals(epic)) {
                System.out.println("такой эпик уже есть!");
                return;
            }
        }
        if (newEpic.getId() == 0){
            newEpic.setId(generateId());
        }
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void newSubtask(Subtask newSubtask) {
        if (!epics.containsKey(newSubtask.getEpicId())) return; //Проверка что есть эпик с таким id
        for (Subtask subtask : subtasks.values()) {
            if (newSubtask.equals(subtask)) {
                System.out.println("Такая подзадача уже есть!");
                return;
            }
        }
        if (newSubtask.getId() == 0){
            newSubtask.setId(generateId());
        }
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.get(newSubtask.getEpicId()).addSubtasksId(newSubtask.getId()); //Добавляем инфу о сабтасках в эпик
        updateEpicStatus(newSubtask.getEpicId());
    }

    @Override
    public void updateTask(Task updateTask) { //Обновление задачи,
        // проверка, что id задачи существует и задача не повторяется
        if (tasks.containsKey(updateTask.getId()) && !updateTask.equals(tasks.get(updateTask.getId()))) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId()) && !updateEpic.equals(epics.get(updateEpic.getId()))) {
            epics.put(updateEpic.getId(), updateEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        if (subtasks.containsKey(updateSubtask.getId()) && !updateSubtask.equals(subtasks.get(updateSubtask.getId()))) {
            subtasks.put(updateSubtask.getId(), updateSubtask);
            updateEpicStatus(updateSubtask.getEpicId()); //Обновляем эпик при обновлении подзадачи
        }
    }

    @Override
    public void deleteTaskById(long id) { //удаление задачи по id
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(long id) {
        if (epics.containsKey(id)) {
            for (Long subtaskId : epics.get(id).getSubtasksId()) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(long id) {
        if (subtasks.containsKey(id)) {
            long idEpic = subtasks.get(id).getEpicId();
            historyManager.remove(id);
            subtasks.remove(id);
            delEpicSubtaskId(idEpic, id);
            updateEpicStatus(idEpic);
        }
    }

    @Override
    public Task getTask(long id) { //Получение задачи по id
        updateHistory(tasks.get(id)); //Обновляем историю просмотра
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(long id) {
        updateHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(long id) {
        updateHistory(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<Subtask> getSubtaskByEpicId(long id) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Long subtaskId : epics.get(id).getSubtasksId()) {
            subtaskArrayList.add(subtasks.get(subtaskId));
        }
        return subtaskArrayList;
    }

    @Override
    public void removeTasks() { //Удаление всех задач
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    public void removeEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear(); //Удаление всех подзадач, тк эпиков больше нет
    }

    @Override
    public void removeSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            clearEpicSubtasksId(epic.getId());
        }
    }

    @Override
    public Map<Long, Task> getTasks() { //Получение списка всех задач
        return new HashMap<>(tasks);
    }

    @Override
    public Map<Long, Epic> getEpics() {
        return new HashMap<>(epics);
    }

    @Override
    public Map<Long, Subtask> getSubtasks() {
        return new HashMap<>(subtasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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

    private void updateHistory(Task task) {
        historyManager.add(task);
    }
}
