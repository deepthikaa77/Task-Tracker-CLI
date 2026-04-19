package repository;
import java.util.List;
import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;
import model.Task;

public class TaskRepository{
    private final String filePath = "tasks.json";

    public TaskRepository(){
        initFile();
    }
    private void initFile(){
        try{
            Path path = Paths.get(filePath);
            if(!Files.exists(path)){
                Files.writeString(path, "[]");
                System.out.println("File created: " + filePath);
            }
        }
        catch(IOException e){
            System.out.println("Error initializing file: " + e.getMessage());
        }
    }

    public void saveTasks(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < tasks.size(); i++) {
            json.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        try {
            Files.writeString(Paths.get(filePath), json.toString());
        } catch (IOException e) {
            System.out.println("Error saving tasks, bro: " + e.getMessage());
        }
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            String content = Files.readString(Paths.get(filePath)).trim();
            if (content.equals("[]") || content.isEmpty()) {
                return tasks;
            }

            content = content.substring(1, content.length() - 1).trim(); // Remove [ and ]

            String[] taskJsons = content.split("},\\s*\\{");

            for(String taskJson : taskJsons){
                taskJson = taskJson.trim();
                if(!taskJson.startsWith("{")){
                    taskJson = "{" + taskJson;
                }
                if(!taskJson.endsWith("}")){
                    taskJson = taskJson + "}";
                }

                try {
                    String idStr = taskJson.replaceAll(".*?\"id\":\\s*(\\d+).*", "$1");
                    String description = taskJson.replaceAll(".*?\"description\":\\s*\"([^\"]+)\".*", "$1");
                    String status = taskJson.replaceAll(".*?\"status\":\\s*\"([^\"]+)\".*", "$1");

                    int id = Integer.parseInt(idStr);
                    Task task = new Task(id, description);
                    task.setStatus(status);
                    tasks.add(task);
                } catch (Exception e) {
                // If one task is corrupted, skip it so the whole app doesn't crash
                    continue; 
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    return tasks;
    }

}