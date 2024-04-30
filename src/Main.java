import entities.Epic;
import entities.Status;
import entities.Subtask;
import entities.Task;
import managers.Managers;
import managers.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.newTask(new Task("Задача 1", "Описание 2 задачи"));
        taskManager.newTask(new Task("Задача 2", "Описание 2 супер задачи"));
        taskManager.newEpic(new Epic("Эпик 1", ""));
        taskManager.newEpic(new Epic("эпик 2", ""));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 1", "ее описание", 3));
        taskManager.newSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание", 3));
        taskManager.newSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание", 4));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtaskByEpicId(3));
        taskManager.updateSubtask(new Subtask("Подзадача 2 эпика 1", "ее описание",
                Status.DONE, 6, 3));
        System.out.println("Проверка после изменения одной подзадачи 1 эпика");
        System.out.println(taskManager.getSubtaskByEpicId(3));
        System.out.println(taskManager.getEpics());
        System.out.println("Проверка после изменения 1 подзадачи 2 эпика");
        taskManager.updateSubtask(new Subtask("Подзадача 1 эпика 2", "ее описание",
                Status.DONE, 7, 4));
        System.out.println(taskManager.getSubtaskByEpicId(4));
        System.out.println(taskManager.getEpics());
        System.out.println("Проверка после удаления 1 эпика:");
        taskManager.deleteEpicById(3);
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        System.out.println("проверка после удаления подзадачи 2 эпика");
        taskManager.deleteSubtaskById(7);
        System.out.println(taskManager.getEpics());
        System.out.println("Проверка истории просмотра");
        System.out.println(taskManager.getTask(1));
        taskManager.getTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(2);
        taskManager.getTask(2);
        taskManager.getTask(2);
        taskManager.getEpic(4);
        taskManager.getTask(2);
        taskManager.getTask(2);
        taskManager.getTask(2);
        taskManager.getTask(2);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(1); //Здесь 1 задача должна пропасть, тк их 10
        System.out.println(taskManager.getHistory());
    }
}