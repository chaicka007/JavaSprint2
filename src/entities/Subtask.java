package entities;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, long id, long epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, LocalDateTime startTime, int duration, long epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, long id, LocalDateTime startTime,
                   int duration, long epicId) {
        super(name, description, status, id, startTime, duration);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Entities.Subtask{" +
                "epicId=" + epicId + super.toString() +
                "} ";
    }
}
