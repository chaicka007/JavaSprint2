package entities;

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
