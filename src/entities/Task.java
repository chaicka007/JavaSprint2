package entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Status status;
    private long id;
    private int duration;
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = entities.Status.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, long id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, Status status, long id, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return startTime.plusMinutes(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, status);
        result = 31 * result;
        return result;
    }

    @Override
    public String toString() {
        return "Entities.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
