package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;


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


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId + super.toString() +
                "} ";
    }
}
