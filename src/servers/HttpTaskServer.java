package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import servers.handlers.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss");
    static FileBackedTasksManager tm = new FileBackedTasksManager("save.csv");
    private static final int PORT = 8080;
    private static final boolean ENABLE_PRETTY_PRINTING = true;
    private static final TasksHandler th = new TasksHandler(gsonCreator(), tm);


    public static void main(String[] args) throws IOException {
        tm.load("save.csv");
        HttpServer httpServer = HttpServer.create();
        Gson gson = gsonCreator();
        System.out.println(gson.toJson(tm.getTasks()));
        System.out.println(gson.toJson(tm.getEpics()));
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", th);
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    private static Gson gsonCreator() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (ENABLE_PRETTY_PRINTING) {
            gsonBuilder.setPrettyPrinting();
        }
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        return gsonBuilder.create();
    }
}
