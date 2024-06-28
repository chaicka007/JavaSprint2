package managers;

import entities.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final static String PATH_FILE = "save.csv";


    public static void main(String[] args) {
        while (true) {
            System.out.println("Что делаем?\n 1. Тестируем сохранение в save.csv \n" +
                    "2. Тестируем загрузку с файла\n" +
                    "3. Тестируем выброс исключения");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    testCreateTasksAndHistory();
                    break;
                case 2:
                    testLoadFromFileAndPrintHistory();
                    break;
                case 3:
                    testException();
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void testCreateTasksAndHistory() {
        FileBackedTasksManager taskManager = new FileBackedTasksManager();
        taskManager.newTask(new Task("Задача 1", "Описание 1 задачи"));
        taskManager.newTask(new Task("Задача 2", "Описание 2 супер задачи"));
        taskManager.newTask(new Task("Задача 3", "Описание 3 супер задачи"));
        taskManager.newTask(new Task("Задача 4", "desc4", Status.NEW, 33));
        taskManager.newEpic(new Epic("Эпик 1", "asasa"));
        taskManager.newEpic(new Epic("эпик 2", "sasa"));
        taskManager.newEpic(new Epic("эпик 3", "sasa"));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 1", "ее описание", 4));
        taskManager.newSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание", 4));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание", 5));
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getTask(7);
        taskManager.getTask(8);
        System.out.println(taskManager.getHistory());
    }

    public static void testLoadFromFileAndPrintHistory() {
        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile("save.csv");
        System.out.println("История " + taskManager.getHistory());
        System.out.println("Таски " + taskManager.getTasks());
        System.out.println("Эпики " + taskManager.getEpics());
        System.out.println("Саб таски " + taskManager.getSubtasks());
    }

    public static void testException() {
        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile("abraKadabra.csv");
    }


    @Override
    public void newTask(Task newTask) {
        super.newTask(newTask);
        save();
    }

    @Override
    public void newEpic(Epic newEpic) {
        super.newEpic(newEpic);
        save();
    }

    @Override
    public Task getTask(long id) {
        super.getTask(id);
        save();
        return super.getTask(id);

    }

    @Override
    public void newSubtask(Subtask newSubtask) {
        super.newSubtask(newSubtask);
        save();
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }

    @Override
    public void deleteTaskById(long id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(long id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(long id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
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
            if (!getHistory().isEmpty()) {
                fw.write("***\n"); // строка разделитель
                fw.write(historyToString(super.historyManager));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения/записи файла");
        }
    }

    public static FileBackedTasksManager loadFromFile(String filePath) {
        FileBackedTasksManager fm = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Пропуск 1 строки с названиями столбцов
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("***")) {
                    line = br.readLine();
                    for (Integer historyId : historyFromString(line)) {
                        fm.historyManager.add(fm.getTask(historyId));
                    }
                } else {
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
            throw new ManagerSaveException("Ошибка чтения/записи файла");
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
