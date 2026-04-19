import repository.TaskRepository;
import model.Task;
import java.util.List;

public class TaskTrackerApp{
    public static void main(String[] args){
        TaskRepository repository = new TaskRepository();
        List<Task> tasks = repository.loadTasks();

        System.out.println("Task Tracker Application Initialized.");
        System.out.println("Enter Commands : add, list, update, delete");
        String command = args[0];

        if(command.equals("list")){
            if(tasks.isEmpty()){
                System.out.println("No tasks found.");
            } else {
                for(Task task : tasks){
                    System.out.println(task.toJson());
                }
            }
        }
        if(args.length >= 2 && args[0].equals("add")){
            String description = args[1];

            // Auto-calculate ID: (Last ID in list + 1)
            int newId = tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;

            Task newTask = new Task(newId, description);
            tasks.add(newTask);
            repository.saveTasks(tasks);

            System.out.println("Task added successfully!");
        }

        if(command.equals("update")){
            if(args.length < 3){
                System.out.println("Usage: update <id> <new_description>");
            }else{
                int id = Integer.parseInt(args[1]);
                String newDescription = args[2];
                boolean found = false;

                for (Task task : tasks) {
                    if (task.getId() == id) {
                        task.setDescription(newDescription);
                        task.setUpdatedAt(java.time.LocalDateTime.now().toString()); // Optional: update timestamp
                        found = true;
                        break;
                    }
                }

                if (found) {
                    repository.saveTasks(tasks);
                    System.out.println("Task updated successfully!");
                } else {
                    System.out.println("Error: Task ID " + id + " not found.");
                }
            }
        }

        if (command.equals("delete")) {
            if (args.length < 2) {
                System.out.println("Error: Please provide task ID to delete.");
            } else {
                int id = Integer.parseInt(args[1]);
                // Remove the task if the ID matches
                boolean removed = tasks.removeIf(task -> task.getId() == id);

                if (removed) {
                    repository.saveTasks(tasks);
                    System.out.println("Task deleted successfully!");
                } else {
                    System.out.println("Error: Task ID " + id + " not found.");
                }
            }
        }

        if (command.equals("mark-in-progress") || command.equals("mark-done")) {
            if (args.length < 2) {
            System.out.println("Error: Please provide task ID.");
        } else {
            int id = Integer.parseInt(args[1]);
            String newStatus = command.equals("mark-done") ? "DONE" : "IN_PROGESS";
            boolean found = false;

            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setStatus(newStatus);
                    task.setUpdatedAt(java.time.LocalDateTime.now().toString());
                    found = true;
                    break;
                }
            }

            if (found) {
                repository.saveTasks(tasks);
                System.out.println("Task status updated successfully!");
            } else {
                System.out.println("Error: Task ID " + id + " not found.");
            }
            }
        }
    }
}