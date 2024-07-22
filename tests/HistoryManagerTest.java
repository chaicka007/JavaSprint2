import entities.Status;
import entities.Task;
import managers.HistoryManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


abstract class HistoryManagerTest<T extends HistoryManager> {
    T manager;

    public HistoryManagerTest(T manager) {
        this.manager = manager;
    }

    @Test
    void testAddAndReturnHistory() {
        Task newTask1 = new Task("task1", "description1", Status.NEW, 1);
        Task newTask2 = new Task("task2", "description2", Status.NEW, 2);
        Task newTask3 = new Task("task3", "description3", Status.NEW, 3);
        manager.add(newTask1);
        manager.add(newTask2);
        manager.add(newTask3);
        assertEquals(3, manager.getHistory().size());
    }

    @Test
    void testAddExistingTask() {
        Task newTask1 = new Task("task1", "description1", Status.NEW, 1);
        manager.add(newTask1);
        manager.add(newTask1);
        manager.add(newTask1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void testRemoveFromHead(){
        Task newTask1 = new Task("task1", "description1", Status.NEW, 1);
        Task newTask2 = new Task("task2", "description2", Status.NEW, 2);
        Task newTask3 = new Task("task3", "description3", Status.NEW, 3);
        manager.add(newTask1);
        manager.add(newTask2);
        manager.add(newTask3);
        manager.remove(1);
        assertEquals(newTask2, manager.getHistory().getFirst());
    }

    @Test
    void testRemoveFromTail(){
        Task newTask1 = new Task("task1", "description1", Status.NEW, 1);
        Task newTask2 = new Task("task2", "description2", Status.NEW, 2);
        Task newTask3 = new Task("task3", "description3", Status.NEW, 3);
        manager.add(newTask1);
        manager.add(newTask2);
        manager.add(newTask3);
        manager.remove(3);
        assertEquals(newTask2, manager.getHistory().getLast());
    }

    @Test
    void testRemoveFromMiddle(){
        Task newTask1 = new Task("task1", "description1", Status.NEW, 1);
        Task newTask2 = new Task("task2", "description2", Status.NEW, 2);
        Task newTask3 = new Task("task3", "description3", Status.NEW, 3);
        manager.add(newTask1);
        manager.add(newTask2);
        manager.add(newTask3);
        manager.remove(2);
        assertEquals(newTask3, manager.getHistory().get(1));
    }
    @Test
    void testGetEmptyHistory(){
        assertEquals(0, manager.getHistory().size());
    }
}