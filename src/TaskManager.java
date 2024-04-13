import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Long, Task> tasks = new HashMap<>();
    HashMap<Long, Epic> epics = new HashMap<>();
    HashMap<Long, Subtask> subtasks = new HashMap<>();
    long idCount = 0;

    public void newTask(Task newTask) { //Создание новой задачи
        for (Task task : tasks.values()) {
            if (newTask.equals(task)) {
                System.out.println("такая задача уже есть");
                return;
            }
        }
        newTask.setId(idCount);
        tasks.put(idCount, newTask);
        idCount++;
    }

    public void newEpic(Epic newEpic) {
        for (Epic epic : epics.values()) {
            if (newEpic.equals(epic)) {
                System.out.println("такой эпик уже есть!");
                return;
            }
        }
        newEpic.setId(idCount);
        epics.put(idCount, newEpic);
        idCount++;
    }

    public void newSubtask(Subtask newSubtask) {
        if (!epics.containsKey(newSubtask.getEpicId())) return; //Проверка что есть эпик с таким id
        for (Subtask subtask : subtasks.values()) {
            if (newSubtask.equals(subtask)) {
                System.out.println("Такая подзадача уже есть!");
                return;
            }
        }
        newSubtask.setId(idCount);
        subtasks.put(idCount, newSubtask);
        updateEpicStatus(newSubtask.getEpicId());
        idCount++;
    }

    public void updateTask(Task updateTask) { //Обновление задачи,
        // проверка что id задачи существует и задача не повторяется
        if (tasks.containsKey(updateTask.getId()) && !updateTask.equals(tasks.get(updateTask.getId()))) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId()) && !updateEpic.equals(epics.get(updateEpic.getId()))) {
            epics.put(updateEpic.getId(), updateEpic);
        }
    }

    private void updateEpicStatus(long id) {
        ArrayList<Task.Status> statuses = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                statuses.add(subtask.getStatus());
            }
        }
        if (statuses.size() > 1) {
            for (int i = 1; i < statuses.size(); i++) { //Сравнение всех статусов подзадач
                if (statuses.get(i).equals(Task.Status.NEW) && statuses.get(i - 1).equals(Task.Status.NEW)) {
                    epics.get(id).setStatus(Task.Status.NEW);
                } else if (statuses.get(i).equals(Task.Status.DONE) && statuses.get(i - 1).equals(Task.Status.DONE)) {
                    epics.get(id).setStatus(Task.Status.DONE);
                } else {
                    epics.get(id).setStatus(Task.Status.IN_PROGRESS);
                    break;
                }
            }
        } else if (statuses.size() == 1) {
            epics.get(id).setStatus(statuses.getFirst());
        } else {
            epics.get(id).setStatus(Task.Status.NEW);
        }
    }

    public void updateSubtask(Subtask updateSubtask) {
        if (subtasks.containsKey(updateSubtask.getId()) && !updateSubtask.equals(subtasks.get(updateSubtask.getId()))) {
            subtasks.put(updateSubtask.getId(), updateSubtask);
            updateEpicStatus(updateSubtask.getEpicId()); //Обвновляем эпик при обновлении подзадачи
        }
    }

    public void deleteTaskById(long id) { //удаление задачи по id
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteEpicById(long id) {
        if (epics.containsKey(id)) {
            ArrayList<Long> deleteId = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId() == id) {
                    deleteId.add(subtask.getId());
                }
            }
            for (Long delete : deleteId) {
                subtasks.remove(delete);
            }
            epics.remove(id);
        }
    }

    public void deleteSubtaskById(long id) {
        if (subtasks.containsKey(id)) {
            long idEpic = subtasks.get(id).getEpicId();
            subtasks.remove(id);
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
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                subtaskArrayList.add(subtask);
            }
        }
        return subtaskArrayList;
    }

    public void removeTasks() { //Удаление всех задач
        tasks.clear();
    }

    public void removeEpics() {
        for (long id : epics.keySet()) {
            deleteEpicById(id);
        }
    }

    public void removeSubtasks() {
        for (long id : subtasks.keySet()) {
            deleteTaskById(id);
        }
    }

    public HashMap<Long, Task> getTasks() { //Получение списка всех задач
        return tasks;
    }

    public HashMap<Long, Epic> getEpics() {
        return epics;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }
}