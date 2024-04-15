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

    public void setSubtasksId(ArrayList<Long> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void addSubtasksId(long id) {
        subtasksId.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId + super.toString()  +
                "} ";
    }
}
