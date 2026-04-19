package model;
import java.time.LocalDateTime;

public class Task{
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status{
        TODO, IN_PROGESS, DONE
    }

    public Task(int id, String description){
        this.id = id;
        this.description = description;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String toJson() {
        return String.format(
            "  {\"id\": %d, \"description\": \"%s\", \"status\": \"%s\", \"createdAt\": \"%s\", \"updatedAt\": \"%s\"}",
            id, description, status, createdAt, updatedAt
        );
    }

    public int getId(){
        return id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = java.time.LocalDateTime.parse(updatedAt);
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status.toUpperCase().replace("-", "_"));
    }


}
