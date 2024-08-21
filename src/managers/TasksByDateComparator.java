package managers;

import entities.Task;

import java.util.Comparator;

public class TasksByDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null && o2.getStartTime() != null) {
            return 1;
        } else if (o1.getStartTime() != null && o2.getStartTime() == null) {
            return -1;
        } else if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return 0;
        }
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        }
        return 0;
    }
}
