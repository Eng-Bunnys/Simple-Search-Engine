package org.bunnys.task;

import org.bunnys.utils.Snowflake;

public class Task {
    private final Snowflake taskID;
    private String taskName;
    private String taskDescription;

    public Task(String taskName, String taskDescription) {
        this.taskID = Snowflake.generateSnowflake(); // Assign ID here
        setTaskName(taskName);
        setTaskDescription(taskDescription);
    }

    public Task(String taskName) {
        this(taskName, null);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        if (taskName == null || taskName.isEmpty())
            throw new IllegalArgumentException("You must provide a value for Task Name");

        if (taskName.length() > 100)
            throw new IllegalArgumentException("Task Name is too long (> 100 characters)");

        if (taskName.length() < 5)
            throw new IllegalArgumentException("Task Name is too short (< 5 characters)");

        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        if (taskDescription != null && taskDescription.length() > 500)
            throw new IllegalArgumentException("Task Description is too long");

        this.taskDescription = taskDescription;
    }

    public Snowflake getTaskID() {
        return taskID;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                '}';
    }
}
