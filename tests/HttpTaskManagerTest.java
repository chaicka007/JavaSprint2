import entities.Epic;
import entities.Subtask;
import entities.Task;
import managers.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.KVServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpTaskManagerTest extends TaskManagerTest<managers.HttpTaskManager> {
    KVServer kvServer;
    HttpTaskManager manager;
    private static final String SAVE_URL = "http://localhost:8078";

    public HttpTaskManagerTest() {
        super(new managers.HttpTaskManager(SAVE_URL, "save.csv"));
    }


    @BeforeEach
    void setup() {
        kvServer = new KVServer();
        kvServer.start();
        manager = new HttpTaskManager(SAVE_URL, "save.csv");
        System.out.println("setup");
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void testSaveAndLoadEmptyTaskList() {

        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());
        assertEquals(0, manager.getHistory().size());
        manager.save();
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(0, loadedFromFileManager.getTasks().size());
        assertEquals(0, loadedFromFileManager.getEpics().size());
        assertEquals(0, loadedFromFileManager.getSubtasks().size());
        assertEquals(0, loadedFromFileManager.getHistory().size());
    }

    @Test
    void testSaveAndLoadEpicWithoutSubtasks() {
        manager.newEpic(new Epic("epic", "description"));
        assertEquals(0, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(0, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
    }

    @Test
    void testSaveAndLoadEpicWithSubtasks() {
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description", 1));
        manager.newSubtask(new Subtask("subtask2", "description", 1));

        assertEquals(2, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(2, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
    }

    @Test
    void testSaveAndLoadHistoryEmpty() {
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description", 1));
        manager.newSubtask(new Subtask("subtask2", "description", 1));
        assertEquals(0, manager.getHistory().size());
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(0, loadedFromFileManager.getHistory().size());
    }

    @Test
    void testSaveAndLoadHistory() {
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description", 1));
        manager.newSubtask(new Subtask("subtask2", "description", 1));
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        assertEquals(3, manager.getHistory().size());
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(3, loadedFromFileManager.getHistory().size());
    }

    @Test
    void testSaveAndLoadTaskEpicSubtaskHistory() {
        manager.newTask(new Task("task", "description"));
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description", 2));
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(3, manager.getHistory().size());
        assertEquals(1, manager.getTasks().size());
        HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
        loadedFromFileManager.load(SAVE_URL);
        assertEquals(1, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
        assertEquals(3, loadedFromFileManager.getHistory().size());
        assertEquals(1, loadedFromFileManager.getTasks().size());
    }

//    @Test
//    void testException() {
//        ManagerSaveException ex = assertThrows(ManagerSaveException.class, new Executable() {
//            @Override
//            public void execute() {
//                HttpTaskManager loadedFromFileManager = new HttpTaskManager(SAVE_URL, "save.csv");
//                loadedFromFileManager.load("abrakadabra.csv");
//            }
//        });
//        assertEquals("Ошибка чтения/записи файла", ex.getMessage());
//    }
}
