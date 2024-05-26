package managers;

import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head; // Указатель на 1 элемент списка
    private Node<Task> tail; // Указатель на последний элемент списка

    @Override
    public void add(Task task) {

        long taskID = task.getId();
        if (historyMap.containsKey(taskID)) {
            if (historyMap.get(taskID) != tail) { //проверяем, что это задача не в конце
                removeNode(historyMap.get(taskID));
                linkLast(task);
            }
        } else {
            linkLast(task);
        }
    }


    @Override
    public void remove(long id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node<Task> i = head; i != null; i = i.next) {
            tasks.add(i.data);
        }
        return tasks;
    }

    private void removeNode(Node<Task> node) {
        final Node<Task> next = node.next;
        final Node<Task> prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.data = null;
    }

}



