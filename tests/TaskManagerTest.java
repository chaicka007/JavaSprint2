import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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


    @Test
    void testDateTimeEpic() {
        Subtask newSubtask = new Subtask("subtask", "descrption", LocalDateTime.parse("2024-07-22 23:23:32", formatter), 20, 1);
        Subtask newSubtask2 = new Subtask("subtask2", "descrption", LocalDateTime.parse("2024-07-23 23:23:32", formatter), 20, 1);
        Subtask newSubtask3 = new Subtask("subtask3", "descrption", LocalDateTime.parse("2024-07-24 23:23:32", formatter), 20, 1);
        Subtask newSubtask4 = new Subtask("name", "des", 1);
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask4);
        manager.newSubtask(newSubtask3);
        manager.newSubtask(newSubtask2);
        manager.newSubtask(newSubtask);
        System.out.println(manager.getTask(1).getStartTime());
        System.out.println(manager.getTask(1).getEndTime());
        System.out.println(manager.getPrioritizedTasks());
        assertEquals(60, manager.getTask(1).getDuration());
    }

    @Test
    void testDateTimeNoCollisionTask() {
        Task newTask = new Task("task", "description", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60 * 1);
        Task newTask2 = new Task("task2", "description", LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60 * 1);
        manager.newTask(newTask);
        manager.newTask(newTask2);
        assertEquals(2, manager.getTasks().size());
    }

    @Test
    void testDateTimeCollisionTask() {
        Task newTask = new Task("task", "description", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60 * 1);
        Task newTask2 = new Task("task2", "description", LocalDateTime.parse("2024-07-22 09:00:00", formatter), 60 * 1);
        manager.newTask(newTask);
        manager.newTask(newTask2);
        assertEquals(1, manager.getTasks().size());
    }
    @Test
    void testDateTimeNoCollisionOnUpdateTask() {
            Task newTask = new Task("task", "description", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60 * 1);
            Task newTask2 = new Task("task2", "description", LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60 * 1);
            manager.newTask(newTask);
            manager.newTask(newTask2);
            Task updatedTask2 = new Task("task2", "description", Status.IN_PROGRESS, 2, LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60 * 1);
            manager.updateTask(updatedTask2);
            assertEquals(updatedTask2, manager.getTask(2));
    }

    @Test
    void testDateTimeCollisionOnUpdateTask() {
        Task newTask = new Task("task", "description", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60 * 1);
        Task newTask2 = new Task("task2", "description", LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60 * 1);
        manager.newTask(newTask);
        manager.newTask(newTask2);
        Task updatedTask2 = new Task("task2", "description", Status.IN_PROGRESS, 2, LocalDateTime.parse("2024-07-22 09:00:00", formatter), 60 * 1);
        manager.updateTask(updatedTask2);
        assertEquals(newTask2, manager.getTask(2));
    }

    @Test
    void testDateTimeNoCollisionSubtask() {
        Subtask newSubtask = new Subtask("subtask 1", "", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60, 1 );
        Subtask newSubtask2 = new Subtask("subtask 2", "", LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60, 1 );
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.newSubtask(newSubtask2);
        assertEquals(2, manager.getSubtasks().size());
    }
    @Test
    void testDateTimeCollisionSubtask() {
        Subtask newSubtask = new Subtask("subtask 1", "", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60, 1 );
        Subtask newSubtask2 = new Subtask("subtask 2", "", LocalDateTime.parse("2024-07-22 08:30:00", formatter), 60, 1 );
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.newSubtask(newSubtask2);
        assertEquals(1, manager.getSubtasks().size());
    }

    @Test
    void testDateTimeCollisionOnUpdateSubtask() {
        Subtask newSubtask = new Subtask("subtask 1", "", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60, 1 );
        Subtask newSubtask2 = new Subtask("subtask 2", "", LocalDateTime.parse("2024-07-22 09:30:00", formatter), 60, 1 );
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.newSubtask(newSubtask2);
        Subtask updateSubtask = new Subtask("subtask 2", "", Status.IN_PROGRESS, 3, LocalDateTime.parse("2024-07-22 08:30:00", formatter), 60, 1 );
        manager.updateSubtask(updateSubtask);
        assertEquals(newSubtask2, manager.getTask(3));
    }

    @Test
    void testDateTimeNoCollisionOnUpdateSubtask() {
        Subtask newSubtask = new Subtask("subtask 1", "", LocalDateTime.parse("2024-07-22 08:00:00", formatter), 60, 1 );
        Subtask newSubtask2 = new Subtask("subtask 2", "", LocalDateTime.parse("2024-07-22 09:30:00", formatter), 60, 1 );
        Epic newEpic = new Epic("epic", "description");
        manager.newEpic(newEpic);
        manager.newSubtask(newSubtask);
        manager.newSubtask(newSubtask2);
        Subtask updateSubtask = new Subtask("subtask 2", "", Status.IN_PROGRESS, 3, LocalDateTime.parse("2024-07-22 06:30:00", formatter), 60, 1 );
        manager.updateSubtask(updateSubtask);
        assertEquals(updateSubtask, manager.getTask(3));
    }
    @Test
    void testTaskWithoutDateTimeTostring(){
        Task newTask = new Task("task", "description");
        Task newTask2 = new Task("task2", "description", LocalDateTime.parse("2024-07-22 09:00:01", formatter), 60 * 1);
        System.out.println(newTask);
        System.out.println(newTask2);
    }
}