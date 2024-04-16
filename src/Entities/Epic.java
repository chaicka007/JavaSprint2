package Entities;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Long> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtasksId(long id) {
        subtasksId.add(id);
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void delSubtaskId(long id) {
        subtasksId.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId + super.toString() +
                "} ";
    }
}
