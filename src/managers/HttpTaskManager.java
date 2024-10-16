package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entities.Epic;
import entities.Subtask;
import entities.Task;
import servers.KVTaskClient;
import servers.LocalDateAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient;

    public HttpTaskManager(String serverUrl, String savePath) {
        super(savePath);
        this.taskClient = new KVTaskClient(serverUrl);
    }

    @Override
    public void load(String serverUrl) {
        Gson gson = gsonCreator();
        Map<Long, Task> loadedTasks = gson.fromJson(taskClient.load("tasks"),
                new TypeToken<Map<Long, Task>>() {
                }.getType());
        Map<Long, Epic> loadedEpics = gson.fromJson(taskClient.load("epics"),
                new TypeToken<Map<Long, Epic>>() {
                }.getType());
        Map<Long, Subtask> loadedSubtasks = gson.fromJson(taskClient.load("subtasks"),
                new TypeToken<Map<Long, Subtask>>() {
                }.getType());
        List<Task> loadedHistory = gson.fromJson(taskClient.load("history"),
                new TypeToken<List<Task>>() {
                }.getType());
        for (Task task : loadedTasks.values()) {
            tasks.put(task.getId(), task);
        }
        for (Epic epic : loadedEpics.values()) {
            epics.put(epic.getId(), epic);
        }
        for (Subtask subtask : loadedSubtasks.values()) {
            subtasks.put(subtask.getId(), subtask);
        }
        for (Task task : loadedHistory) {
            historyManager.add(task);
        }

    }

    @Override
    public void save() {
        Gson gson = gsonCreator();
        String taskGson = gson.toJson(getTasks());
        String epicGson = gson.toJson(getEpics());
        String subtaskGson = gson.toJson(getSubtasks());
        String historyGson = gson.toJson(getHistory());
        taskClient.put("tasks", taskGson);
        taskClient.put("epics", epicGson);
        taskClient.put("subtasks", subtaskGson);
        taskClient.put("history", historyGson);
    }

    private static Gson gsonCreator() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        return gsonBuilder.create();
    }
}
