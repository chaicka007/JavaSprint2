package managers;

import entities.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final static String PATH_FILE = "save.csv";


    public static void main(String[] args) {
          FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile();
//        FileBackedTasksManager taskManager = new FileBackedTasksManager();
//        taskManager.newTask(new Task("Задача 1", "Описание 1 задачи"));
//        taskManager.newTask(new Task("Задача 2", "Описание 2 супер задачи"));
//        taskManager.newTask(new Task("Задача 3", "Описание 3 супер задачи"));
//        taskManager.newTask(new Task("Задача 4", "desc4", Status.NEW, 33));
//        taskManager.newEpic(new Epic("Эпик 1", "asasa"));
//        taskManager.newEpic(new Epic("эпик 2", "sasa"));
//        taskManager.newEpic(new Epic("эпик 3", "sasa"));
//        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 1", "ее описание", 4));
//        taskManager.newSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание", 4));
//        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание", 5));
//        taskManager.getTask(1);
//        taskManager.getTask(2);
//        taskManager.getTask(3);
//        taskManager.getTask(1);
//        taskManager.getTask(1);
//        System.out.println(taskManager.getTask(33));
//        System.out.println(taskManager.getHistory()); //Должен быть список просмотра без повтора в порядке 2,3,1
//        taskManager.deleteTaskById(2);
//        System.out.println(taskManager.getHistory()); //Должен быть список просмотра без повтора в порядке 3,1
//        taskManager.getTask(6);
//        taskManager.getTask(6);
//        System.out.println(taskManager.getHistory()); //3,1,6
//        taskManager.getTask(4);
//        taskManager.getTask(7);
//        taskManager.getTask(8);
//        System.out.println(taskManager.getHistory()); //3,1,6,4,7,8
//        taskManager.save();
//        System.out.println(taskManager.getTasks());
//        System.out.println(taskManager.getEpics());
//        System.out.println(taskManager.getSubtasks());
          System.out.println(taskManager.getHistory());

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
        try (FileWriter fw = new FileWriter(PATH_FILE)) {
            fw.write(String.join(",", "id", "type", "name", "status", "description", "epic") + "\n");
            for (Task task : getTasks().values()) {
                fw.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics().values()) {
                fw.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks().values()) {
                fw.write(toString(subtask) + "\n");
            }
            fw.write("***\n"); // строка разделитель
            fw.write(historyToString(super.historyManager));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager fm = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_FILE))) {
            br.readLine(); // Пропуск 1 строки с названиями столбцов
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("***")) {
                    line = br.readLine();
                    for (Integer historyId : historyFromString(line)){
                        fm.historyManager.add(fm.getTask(historyId));
                    }
                } else{
                    Task entity = fromString(line);
                    if (entity instanceof Subtask) {
                        fm.newSubtask((Subtask) entity);
                    } else if (entity instanceof Epic) {
                        fm.newEpic((Epic) entity);
                    } else {
                        fm.newTask(entity);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fm;
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",").append(",").append(task.getName()).append(",")
                .append(task.getStatus()).append(",").append(task.getDescription());
        if (task instanceof Subtask) {
            sb.insert(sb.indexOf(",") + 1, Type.SUBTASK).append(",").append(((Subtask) task).getEpicId());
            return sb.toString();
        } else if (task instanceof Epic) {
            sb.insert(sb.indexOf(",") + 1, Type.EPIC);
            return sb.toString();
        } else {
            sb.insert(sb.indexOf(",") + 1, Type.TASK);
            return sb.toString();
        }
    }

    private static Task fromString(String value) {
        String[] valueArr = value.split(",");
        int id = Integer.parseInt(valueArr[0]);
        Type type = Type.valueOf(valueArr[1]);
        String name = valueArr[2];
        Status status = Status.valueOf(valueArr[3]);
        String description = valueArr[4];
        return switch (type) {
            case Type.TASK -> new Task(name, description, status, id);
            case Type.EPIC -> new Epic(name, description, status, id);
            case Type.SUBTASK -> new Subtask(name, description, status, id, Integer.parseInt(valueArr[5]));
        };
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String s : value.split(",")) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }
}
