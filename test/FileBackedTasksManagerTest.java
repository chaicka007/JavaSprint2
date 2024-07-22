import entities.Epic;
import entities.Subtask;
import entities.Task;
import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager("save.csv"));
    }
    FileBackedTasksManager manager = new FileBackedTasksManager("save.csv");

    @Test
    void testSaveAndLoadEmptyTaskList(){
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());
        assertEquals(0, manager.getHistory().size());
        manager.save();
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(0, loadedFromFileManager.getTasks().size());
        assertEquals(0, loadedFromFileManager.getEpics().size());
        assertEquals(0, loadedFromFileManager.getSubtasks().size());
        assertEquals(0, loadedFromFileManager.getHistory().size());
    }

    @Test
    void testSaveAndLoadEpicWithoutSubtasks(){
        manager.newEpic(new Epic("epic", "description"));
        assertEquals(0, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(0, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
    }

    @Test
    void testSaveAndLoadEpicWithSubtasks(){
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description",1));
        manager.newSubtask(new Subtask("subtask2", "description",1));

        assertEquals(2, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(2, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
    }
    @Test
    void testSaveAndLoadHistoryEmpty(){
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description",1));
        manager.newSubtask(new Subtask("subtask2", "description",1));
        assertEquals(0, manager.getHistory().size());
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(0, loadedFromFileManager.getHistory().size());
    }

    @Test
    void testSaveAndLoadHistory(){
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description",1));
        manager.newSubtask(new Subtask("subtask2", "description",1));
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        assertEquals(3, manager.getHistory().size());
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(3, loadedFromFileManager.getHistory().size());
    }
    @Test
    void testSaveAndLoadTaskEpicSubtaskHistory(){
        manager.newTask(new Task("task", "description"));
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "description",2));
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(3, manager.getHistory().size());
        assertEquals(1, manager.getTasks().size());
        FileBackedTasksManager loadedFromFileManager = FileBackedTasksManager.loadFromFile("save.csv");
        assertEquals(1, loadedFromFileManager.getSubtasks().size());
        assertEquals(1, loadedFromFileManager.getEpics().size());
        assertEquals(3, loadedFromFileManager.getHistory().size());
        assertEquals(1, loadedFromFileManager.getTasks().size());
    }
}