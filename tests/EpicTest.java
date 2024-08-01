import entities.Epic;
import entities.Status;
import entities.Subtask;
import managers.TaskManager;
import managers.TaskManagersCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class EpicTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach(){
        taskManager = TaskManagersCreator.getDefault();
    }

    @Test
    public void testEpicWithoutSubtasksShouldReturnNew(){
        taskManager.newEpic(new Epic("Новый эпик без подзадач", "описание"));
        Assertions.assertEquals(Status.NEW, taskManager.getTask(1).getStatus());
    }

    @Test
    public void testEpicWithNewSubtasksShouldReturnNew(){
        taskManager.newEpic(new Epic("Новый эпик без подзадач", "описание"));
        taskManager.newSubtask(new Subtask("Сабтаска№1 1 эпика", "", 1));
        taskManager.newSubtask(new Subtask("Сабтаска№2 1 эпика", "", 1));
        assertEquals(Status.NEW, taskManager.getTask(1).getStatus());
    }

    private void assertEquals(Status status, Status status1) {
    }

    @Test
    public void testEpicWithNDoneSubtasksShouldReturnDone(){
        taskManager.newEpic(new Epic("Новый эпик без подзадач", "описание"));
        taskManager.newSubtask(new Subtask("Сабтаска№1 1 эпика", "", 1));
        taskManager.newSubtask(new Subtask("Сабтаска№2 1 эпика", "", 1));
        taskManager.updateSubtask(new Subtask("Сабтаска№1 1 эпика", "", Status.DONE, 2,1));
        taskManager.updateSubtask(new Subtask("Сабтаска№2 1 эпика", "", Status.DONE,3,1));
        assertEquals(Status.DONE, taskManager.getTask(1).getStatus());
    }

    @Test
    public void testEpicWithNDoneAndNewSubtasksShouldReturnInProgress(){
        taskManager.newEpic(new Epic("Новый эпик без подзадач", "описание"));
        taskManager.newSubtask(new Subtask("Сабтаска№1 1 эпика", "", 1));
        taskManager.newSubtask(new Subtask("Сабтаска№2 1 эпика", "", 1));
        taskManager.updateSubtask(new Subtask("Сабтаска№1 1 эпика", "", Status.DONE, 2,1));
        assertEquals(Status.IN_PROGRESS, taskManager.getTask(1).getStatus());
    }

    @Test
    public void testEpicWithInProgressSubtasksShouldReturnInProgress(){
        taskManager.newEpic(new Epic("Новый эпик без подзадач", "описание"));
        taskManager.newSubtask(new Subtask("Сабтаска№1 1 эпика", "", 1));
        taskManager.updateSubtask(new Subtask("Сабтаска№1 1 эпика", "", Status.IN_PROGRESS, 2,1));
        assertEquals(Status.IN_PROGRESS, taskManager.getTask(1).getStatus());
    }
}