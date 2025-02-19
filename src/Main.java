package org.bunnys;

import org.bunnys.task.Task;
import org.bunnys.task.manager.TaskManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        manager.addTask(new Task("I love Java programming", "Learning Java basics"));
        manager.addTask(new Task("Go to the gym today", "Cardio workout"));
        manager.addTask(new Task("Complete gym workout routine", "Full body workout"));
        manager.addTask(new Task("Advanced Java programming practice", "Working on Spring Boot"));
        manager.addTask(new Task("Grocery shopping at Walmart", "Buy vegetables and fruits"));
        manager.addTask(new Task("Learn Spring Framework", "Study dependency injection"));
        manager.addTask(new Task("Weekly meal prep", "Prepare healthy meals"));
        manager.addTask(new Task("Java design patterns study", "Learning factory pattern"));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Bunny's Search Engine!");
        System.out
                .println("You can search for tasks by entering a search query, this is obviously a practice project.");

        while (true) {
            System.out.print("\nEnter search query (or 'exit' to quit): ");
            String query = scanner.nextLine().trim();

            if (query.equalsIgnoreCase("exit"))
                break;

            if (query.isEmpty()) {
                System.out.println("Please enter a search term.");
                continue;
            }

            List<Task> results = manager.searchTasks(query);

            if (results.isEmpty()) {
                Optional<String> suggestion = manager.findClosestMatch(query);

                if (suggestion.isPresent()) {
                    System.out.println("No exact matches found. Did you mean: " + suggestion.get() + "?");
                    System.out.print("Would you like to search for this instead? (y/n): ");
                    String response = scanner.nextLine().trim().toLowerCase();

                    if (response.equals("y"))
                        results = manager.searchTasks(suggestion.get());

                } else
                    System.out.println("No matching tasks found.");
            }

            if (!results.isEmpty()) {
                System.out.println("\nSearch results (ranked by relevance):");
                System.out.println("--------------------------------");

                for (int i = 0; i < results.size(); i++) {
                    Task task = results.get(i);
                    System.out.printf("%d. %s%n", i + 1, task.getTaskName());

                    if (task.getTaskDescription() != null && !task.getTaskDescription().isEmpty())
                        System.out.printf("   Description: %s%n", task.getTaskDescription());

                    System.out.println();
                }

                System.out.printf("Found %d matching tasks.%n", results.size());
            }
        }

        scanner.close();
    }
}
