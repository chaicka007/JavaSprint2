import Entities.Epic;
import Entities.Status;
import Entities.Subtask;
import Entities.Task;
import Managers.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.newTask(new Task("Задача 1", "Описание 2 задачи"));
        taskManager.newTask(new Task("Задача 2", "Описание 2 супер задачи"));
        taskManager.newEpic(new Epic("Эпик 1", ""));
        taskManager.newEpic(new Epic("эпик 2", ""));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 1", "ее описание", 2));
        taskManager.newSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание", 2));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание", 3));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtaskByEpicId(2));
        taskManager.updateSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание",
                Status.DONE, 5, 2));
        System.out.println("Проверка после изменения одной подзадачи 1 эпика");
        System.out.println(taskManager.getSubtaskByEpicId(2));
        System.out.println(taskManager.getEpics());
        System.out.println("Проверка после изменения 1 подзадачи 2 эпика");
        taskManager.updateSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание",
                Status.DONE, 6, 3));
        System.out.println(taskManager.getSubtaskByEpicId(3));
        System.out.println(taskManager.getEpics());
        System.out.println("Проверка после удаления 1 эпика:");
        taskManager.deleteEpicById(2);
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        System.out.println("проверка после удаления подзадачи 2 эпика");
        taskManager.deleteSubtaskById(6);
        System.out.println(taskManager.getEpics());
    }
}