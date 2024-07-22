package managers;

import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import entities.TypeTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String PATH_FILE;
    private static final String SEP = ",";
    private static final String STRING_SEP = "***";

    public FileBackedTasksManager(String savePath) {
        PATH_FILE = savePath;
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
            fw.write(String.join(SEP, "id", "type", "name", "status", "description",
                    "startTime", "duration", "endTime", "epic") + "\n");
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
                fw.write(STRING_SEP + "\n");
                fw.write(historyToString(super.historyManager));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения/записи файла");
        }
    }

    public static FileBackedTasksManager loadFromFile(String filePath) {
        FileBackedTasksManager fm = new FileBackedTasksManager(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Пропуск 1 строки с названиями столбцов
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals(STRING_SEP)) {
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
        sb.append(task.getId()).append(SEP).append(task.getClass().getSimpleName().toUpperCase())
                .append(SEP).append(task.getName()).append(SEP).append(task.getStatus())
                .append(SEP).append(task.getDescription()).append(SEP).append(task.getStartTime())
                .append(SEP).append(task.getDuration());
        if (task instanceof Subtask) {
            sb.append(SEP).append(((Subtask) task).getEpicId());
            return sb.toString();
        }
        return sb.toString();
    }

    private static Task fromString(String value) {
        String[] valueArr = value.split(SEP);
        int id = Integer.parseInt(valueArr[0]);
        TypeTask typeTask = TypeTask.valueOf(valueArr[1]);
        String name = valueArr[2];
        Status status = Status.valueOf(valueArr[3]);
        String description = valueArr[4];
        LocalDateTime startTime = (!Objects.equals(valueArr[5], "null")) ? LocalDateTime.parse(valueArr[5]) : null;
        int duration = Integer.parseInt(valueArr[6]);
        return switch (typeTask) {
            case TypeTask.TASK -> new Task(name, description, status, id, startTime, duration);
            case TypeTask.EPIC -> new Epic(name, description, status, id);
            case TypeTask.SUBTASK ->
                    new Subtask(name, description, status, id, startTime, duration, Integer.parseInt(valueArr[7]));
        };
    }

    private String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(SEP);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String s : value.split(SEP)) {
            history.add(Integer.parseInt(s));
        }
        return history;
    }
}
