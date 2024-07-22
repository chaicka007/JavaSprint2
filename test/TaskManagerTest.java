import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }


    @Test
    void testCreateExistingTask() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        manager.newTask(newTask);
        manager.newTask(newTask);
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    void testCreateExistingEpic() {
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newEpic(newEpic);
        manager.newEpic(newEpic);
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    void testCreateExistingSubtask() {
        Epic newEpic = new Epic("epic", "description");
        Subtask newSubtask = new Subtask("subtask", "descrption", 1);
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.newSubtask(newSubtask);
        assertEquals(1, manager.getSubtasks().size());
    }

    @Test
    void testUpdateExistingTask() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        Task updatedTask = new Task("updated", "description", Status.NEW, 1);
        manager.updateTask(updatedTask);
        assertEquals(updatedTask, manager.getTask(1));
    }

    @Test
    void testUpdateNonExistingTask() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        Task updatedTask = new Task("updated", "description", Status.NEW, 3);
        manager.updateTask(updatedTask);
        assertNotEquals(updatedTask, manager.getTask(1));
        assertEquals(1, manager.getTasks().size());
    }

    @Test
    void testUpdateExistingEpic() {
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        Epic updatedEpic = new Epic("updated", "description", Status.NEW, 1);
        manager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, manager.getTask(1));
    }

    @Test
    void testUpdateNonExistingEpic() {
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        Epic updatedEpic = new Epic("updated", "description", Status.NEW, 131);
        manager.updateEpic(updatedEpic);
        assertNotEquals(updatedEpic, manager.getTask(1));
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    void testDeleteExistingTask() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        manager.deleteTaskById(1);
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void testDeleteExistingEpic() {
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.deleteEpicById(1);
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    void testDeleteExistingEpicAmdSubtasks() {
        Epic newEpic = new Epic("epic", "description");
        Subtask newSubtask = new Subtask("subtask", "descrption", 1);
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.deleteEpicById(1);
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testDeleteExistingSubtask() {
        Epic newEpic = new Epic("epic", "description");
        Subtask newSubtask = new Subtask("subtask", "descrption", 1);
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.deleteSubtaskById(2);
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testGetSubtaskByEpicId() {
        Epic newEpic = new Epic("epic", "description");
        Subtask newSubtask = new Subtask("subtask", "descrption", 1);
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        assertEquals(newSubtask, manager.getSubtaskByEpicId(newEpic.getId()).getFirst());
    }

    @Test
    void testRemoveAllExistingSubtasks() {
        Epic newEpic = new Epic("epic", "description");
        Subtask newSubtask1 = new Subtask("subtask", "descrption", 1);
        Subtask newSubtask2 = new Subtask("subtask2", "descrption2", 1);
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask1);
        manager.newSubtask(newSubtask2);
        assertEquals(2, manager.getSubtasks().size());
        manager.removeSubtasks();
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testRemoveAllNExistingTasks() {
        manager.newTask(new Task("task", "description"));
        manager.newTask(new Task("task2", "description2"));
        manager.removeTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    void testRemoveAllExistingEpic() {
        manager.newEpic(new Epic("epic", "description"));
        manager.newSubtask(new Subtask("subtask", "descrption", 1));
        manager.newSubtask(new Subtask("subtask2", "descrption2", 1));
        manager.removeEpics();
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testAddNewTask() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        assertNotNull(manager.getTask(1));
        assertEquals(newTask, manager.getTask(1));
    }

    @Test
    void testAddNewEpic() {
        Task newTask = new Epic("epic", "description");
        manager.newTask(newTask);
        assertNotNull(manager.getTask(1));
        assertEquals(newTask, manager.getTask(1));
    }

    @Test
    void testAddNewEpicAndSubtask() {
        Epic newTask = new Epic("epic", "description");
        Subtask newSubtask = new Subtask("subtask", "description", 1);
        manager.newEpic(newTask);
        manager.newSubtask(newSubtask);
        assertNotNull(manager.getTask(1));
        assertEquals(newTask, manager.getTask(1));
        assertEquals(newSubtask, manager.getTask(2));
        Subtask subtask = (Subtask) manager.getTask(2);
        assertEquals(1, subtask.getEpicId());
    }

    @Test
    void testGetAllTasks() {
        Task newTask = new Task("task", "description");
        manager.newTask(newTask);
        assertNotNull(manager.getTasks());
        assertEquals(newTask, manager.getTasks().get(1L));
    }

    @Test
    void testGetAllTasksShouldReturnEmptyListWithoutTasks() {
        assertEquals("{}", manager.getTasks().toString());
    }

    @Test
    void testGetAllEpics() {
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        assertNotNull(manager.getEpics());
        assertEquals(newEpic, manager.getEpics().get(1L));
    }

    @Test
    void testGetAllEpicsShouldReturnEmptyListWithoutEpics() {
        assertEquals("{}", manager.getEpics().toString());
    }

    @Test
    void testGetNonExistTaskOrEpicOrSubtask() {
        assertNull(manager.getTask(2));
    }


}