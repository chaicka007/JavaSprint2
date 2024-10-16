package servers.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import exceptions.EqualsTaskExistException;
import managers.TaskManager;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static servers.HttpTaskServer.DATE_TIME_FORMATTER;

public class TasksHandler implements HttpHandler {
    private final String ERROR_INVALID_REQUEST = "Неверный запрос!";
    private final String ERROR_INVALID_METHOD = "Неверный метод!";
    Gson gson;
    TaskManager tm;

    public TasksHandler(Gson gson, TaskManager tm) {
        this.gson = gson;
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        String requestQuery = exchange.getRequestURI().getQuery();
        String requestMethod = exchange.getRequestMethod();
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String requestBody = br.lines().collect(Collectors.joining()); // Чтение тела запроса
        Pair<String, Integer> responseAndCode;
        switch (requestMethod) {
            case "GET":
                responseAndCode = getMapper(requestPath, requestQuery);
                break;
            case "POST":
                responseAndCode = postMapper(requestPath, requestQuery, requestBody);
                break;
            case "DELETE":
                responseAndCode = deleteMapper(requestPath, requestQuery);
                break;
            default:
                responseAndCode = new Pair<>(ERROR_INVALID_METHOD, 405);
        }

        int rCode = responseAndCode.getValue1();
        String response = responseAndCode.getValue0();
        exchange.sendResponseHeaders(rCode, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private Pair<String, Integer> getMapper(String requestPath, String requestQuery) {
        String[] requestArr = requestPath.split("/");
        Map<String, String> query = queryToMap(requestQuery);

        if (requestArr.length == 2) {
            return new Pair<>(gson.toJson(tm.getPrioritizedTasks()), 200); // GET /tasks/
        } else {
            switch (requestArr[2]) {
                case "task":
                    if (query != null && query.containsKey("id")) {
                        return new Pair<>(gson.toJson(tm.getTask(Long.parseLong(query.get("id")))), 200);
                    }
                    return new Pair<>(gson.toJson(tm.getTasks()), 200);
                case "subtask":
                    if (requestArr.length > 3 && requestArr[3].contains("epic")) {
                        if (query != null && query.containsKey("id")) { // /tasks/subtask/epic/?id=
                            return new Pair<>(gson.toJson(tm.getSubtaskByEpicId(Long.parseLong(query.get("id")))), 200);
                        }
                    }
                    if (query != null && query.containsKey("id")) {
                        return new Pair<>(gson.toJson(tm.getTask(Long.parseLong(query.get("id")))), 200);
                    }
                    return new Pair<>(gson.toJson((tm.getSubtasks())), 200);
                case "epic":
                    if (query != null && query.containsKey("id")) {
                        return new Pair<>(gson.toJson(tm.getTask(Long.parseLong(query.get("id")))), 200);
                    }
                    return new Pair<>(gson.toJson((tm.getEpics())), 200);
                case "history":
                    return new Pair<>(gson.toJson(tm.getHistory()), 200);
            }

        }
        return new Pair<>(ERROR_INVALID_REQUEST, 404);
    }

    private Pair<String, Integer> deleteMapper(String requestPath, String requestQuery) {
        String[] requestArr = requestPath.split("/");
        Map<String, String> query = queryToMap(requestQuery);
        if (requestArr.length > 2) {
            switch (requestArr[2]) {
                case "task":
                    if (query != null && query.containsKey("id")) {
                        tm.deleteTaskById(Long.parseLong(query.get("id")));
                        return new Pair<>("ok", 200);
                    }
                    tm.removeTasks();
                    return new Pair<>("ok", 200);
                case "subtask":
                    if (query != null && query.containsKey("id")) {
                        tm.deleteSubtaskById(Long.parseLong(query.get("id")));
                        return new Pair<>("ok", 200);
                    }
                    tm.removeSubtasks();
                    return new Pair<>("ok", 200);
                case "epic":
                    if (query != null && query.containsKey("id")) {
                        tm.deleteEpicById(Long.parseLong(query.get("id")));
                        return new Pair<>("ok", 200);
                    }
                    tm.removeEpics();
                    return new Pair<>("ok", 200);

            }
        }
        return new Pair<>(ERROR_INVALID_REQUEST, 405);
    }

    private Pair<String, Integer> postMapper(String requestPath, String requestQuery, String requestBody) {
        String[] requestArr = requestPath.split("/");
        Map<String, String> query = queryToMap(requestQuery);
        JsonElement jsonElement = JsonParser.parseString(requestBody);
        if (!jsonElement.isJsonObject() || requestArr.length <= 2) { // проверка эндпоинта и переданных данных
            System.out.println(requestBody);
            return new Pair<>(ERROR_INVALID_REQUEST, 405);
        }
        boolean hasDurationAndStartTime = true;
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
        String description = jsonElement.getAsJsonObject().get("description").getAsString();
        Integer duration = null;
        LocalDateTime startTime = null;
        Status status;
        try {
            duration = jsonElement.getAsJsonObject().get("duration").getAsInt();
            startTime = LocalDateTime.parse(jsonElement.getAsJsonObject().get("startTime").getAsString(),
                    DATE_TIME_FORMATTER);
        } catch (Exception e) {
            hasDurationAndStartTime = false; // Проверка правильности полей duration и startTime или наличия
        }

        try {
            switch (requestArr[2]) {
                case "task":
                    if (query != null && query.containsKey("id")) {
                        status = Status.valueOf(jsonElement.getAsJsonObject().get("status").getAsString());
                        if (hasDurationAndStartTime) {
                            tm.updateTask(new Task(name, description, status, Long.parseLong(query.get("id")),
                                    startTime, duration));
                        } else {
                            tm.updateTask(new Task(name, description, status, Long.parseLong(query.get("id"))));
                        }
                        return new Pair<>("updated!", 201);
                    }
                    if (hasDurationAndStartTime) {
                        tm.newTask(new Task(name, description, startTime, duration));
                    } else {
                        tm.newTask(new Task(name, description));
                    }
                    return new Pair<>("created!", 201);

                case "subtask":
                    long epicId = jsonElement.getAsJsonObject().get("epicId").getAsLong();
                    if (query != null && query.containsKey("id")) {
                        status = Status.valueOf(jsonElement.getAsJsonObject().get("status").getAsString());
                        if (hasDurationAndStartTime) {
                            tm.updateSubtask(new Subtask(name, description, status, Long.parseLong(query.get("id")),
                                    startTime, duration, epicId));
                        } else {
                            tm.updateSubtask(new Subtask(name, description, status, Long.parseLong(query.get("id")),
                                    epicId));
                        }
                        return new Pair<>("updated!", 201);
                    }
                    if (hasDurationAndStartTime) {
                        tm.newSubtask(new Subtask(name, description, startTime, duration, epicId));
                    } else {
                        tm.newSubtask(new Subtask(name, description, epicId));
                    }
                    return new Pair<>("created!", 201);
                case "epic":
                    if (query != null && query.containsKey("id")) {
                        tm.updateEpic(new Epic(name, description, Long.parseLong(query.get("id"))));
                        return new Pair<>("updated!", 201);
                    }
                    tm.newEpic(new Epic(name, description));
                    return new Pair<>("created!", 201);
            }
        } catch (EqualsTaskExistException e) {
            return new Pair<>(e.getMessage(), 409);
        } catch (Exception e) {
            return new Pair<>(ERROR_INVALID_REQUEST, 400);
        }
        return new Pair<>(ERROR_INVALID_REQUEST, 400);
    }


}
