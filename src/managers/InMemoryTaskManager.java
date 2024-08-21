package managers;

import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = HistoryManagersCreator.getDefaultHistory();
    private long idCount = 0;

    private void generateId(Task task) {
        if (task.getId() == 0) {
            task.setId(++idCount);
        }
    }


    @Override
    public void newTask(Task newTask) { //Создание новой задачи
        for (Task task : tasks.values()) {
            if (newTask.equals(task)) {
                System.out.println("такая задача уже есть");
                return;
            }
        }
        if (isDataTimeCollisionInTask(newTask)) {
            throw new DataTimeCollisionException("Задача не добавлена: пересечение времени!");
        }
        generateId(newTask);
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
        generateId(newEpic);
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void newSubtask(Subtask newSubtask) {
        if (!epics.containsKey(newSubtask.getEpicId())) return; //Проверка, что есть эпик с таким id
        for (Subtask subtask : subtasks.values()) {
            if (newSubtask.equals(subtask)) {
                System.out.println("Такая подзадача уже есть!");
                return;
            }
        }
        if (isDataTimeCollisionInTask(newSubtask)) {
            throw new DataTimeCollisionException("Задача не добавлена: пересечение времени!");
        }
        generateId(newSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
        epics.get(newSubtask.getEpicId()).addSubtasksId(newSubtask.getId()); //Добавляем инфу о сабтасках в эпик
        updateEpicStatus(newSubtask.getEpicId());
        updateEpicStartAndEndTime(newSubtask.getEpicId());
    }

    @Override
    public void updateTask(Task updateTask) { //Обновление задачи,
        // проверка, что id задачи существует и задача не повторяется
        if (tasks.containsKey(updateTask.getId()) && !updateTask.equals(tasks.get(updateTask.getId()))) {
            Task oldTask = tasks.get(updateTask.getId()); //Запоминаем старую версию таски
            tasks.remove(oldTask.getId());
            if (isDataTimeCollisionInTask(updateTask)) {
                tasks.put(oldTask.getId(), oldTask); // Возвращаем старую задачу если не получилось обновить
                throw new DataTimeCollisionException("Задача не обновлена: пересечение времени!");
            }
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
            Subtask oldSubtask = subtasks.get(updateSubtask.getId());
            subtasks.remove(oldSubtask.getId());
            if (isDataTimeCollisionInTask(updateSubtask)) {
                subtasks.put(oldSubtask.getId(), oldSubtask);
                throw new DataTimeCollisionException("Задача не обновлена: пересечение времени!");
            }
            subtasks.put(updateSubtask.getId(), updateSubtask);
            updateEpicStatus(updateSubtask.getEpicId()); //Обновляем эпик при обновлении подзадачи
            updateEpicStartAndEndTime(updateSubtask.getEpicId());
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
            updateEpicStartAndEndTime(idEpic);
        }
    }

    @Override
    public Task getTask(long id) { //Получение задачи по id
        if (tasks.containsKey(id)) {
            updateHistory(tasks.get(id)); //Обновляем историю просмотра
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            updateHistory(epics.get(id));
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            updateHistory(subtasks.get(id));
            return subtasks.get(id);
        }
        return null;
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

    @Override
    public List<Task> getPrioritizedTasks() {
        final Set<Task> sortedTasksByTime = new TreeSet<>(new TasksByDateComparator());
        sortedTasksByTime.addAll(tasks.values());
        sortedTasksByTime.addAll(subtasks.values());
        return new ArrayList<>(sortedTasksByTime);
    }

    private boolean isDataTimeCollisionInTask(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }
        for (Task sortedTask : getPrioritizedTasks()) {
            if (sortedTask.getStartTime() == null) {
                continue;
            }
            if (task.getStartTime().isAfter(sortedTask.getStartTime()) && task.getStartTime()
                    .isBefore(sortedTask.getEndTime())) {
                return true;
            } else if (task.getEndTime().isAfter(sortedTask.getStartTime()) && task.getEndTime()
                    .isBefore(sortedTask.getEndTime())) {
                return true;
            } else if (task.getStartTime().isEqual(sortedTask.getStartTime()) || task.getEndTime()
                    .isEqual(sortedTask.getEndTime()) || task.getStartTime().isEqual(sortedTask.getEndTime())
                    || task.getEndTime().isEqual(sortedTask.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    private void updateEpicStartAndEndTime(long id) {
        Epic epic = epics.get(id);
        if (epic.getSubtasksId().isEmpty()) return;
        if (epic.getSubtasksId().size() == 1) {
            epic.setStartTime(subtasks.get(epic.getSubtasksId().getFirst()).getStartTime());
            epic.setDuration(subtasks.get(epic.getSubtasksId().getFirst()).getDuration());
            epic.setEndTime(subtasks.get(epic.getSubtasksId().getFirst()).getEndTime());
        } else {
            List<Subtask> subtasksOfEpic = new ArrayList<>();
            int allDuration = 0;
            for (Long subtaskId : epic.getSubtasksId()) {
                if (subtasks.get(subtaskId).getStartTime() != null && subtasks.get(subtaskId).getEndTime() != null) {
                    subtasksOfEpic.add(subtasks.get(subtaskId));
                    allDuration += subtasks.get(subtaskId).getDuration();
                    // Проверка что есть инфа о времени старта
                }

            }
            epic.setDuration(allDuration);
            if (subtasksOfEpic.isEmpty()) {
                return;
            }
            List<Subtask> sortedSubtasksOfEpicByStartTime = subtasksOfEpic.stream()
                    .sorted(Comparator.comparing(Subtask::getStartTime))
                    .toList();
            List<Subtask> sortedSubtasksOfEpicByEndTime = subtasksOfEpic.stream()
                    .sorted(Comparator.comparing(Subtask::getEndTime).reversed())
                    .toList();

            epic.setStartTime(sortedSubtasksOfEpicByStartTime.getFirst().getStartTime());
            epic.setDuration(allDuration);
            epic.setEndTime(sortedSubtasksOfEpicByEndTime.getFirst().getEndTime());
        }
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
