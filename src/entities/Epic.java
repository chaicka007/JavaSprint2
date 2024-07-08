package entities;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status, long id) {
        super(name, description, status, id);
    }

    public ArrayList<Long> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtasksId(long id) {
        subtasksId.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId + super.toString() +
                "} ";
    }
}
