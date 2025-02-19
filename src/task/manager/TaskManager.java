package org.bunnys.task.manager;

import org.bunnys.task.Task;
import org.bunnys.utils.Snowflake;
import org.bunnys.utils.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManager {
    // Storing all our tasks with their IDs for quick lookup
    private final Map<Snowflake, Task> tasks = new ConcurrentHashMap<>();
    // Helps us search faster by keeping track of words and their tasks
    private final Map<String, Set<Task>> invertedIndex = new ConcurrentHashMap<>();
    // Max typos we allow in fuzzy search - 2 is good enough I think
    private static final int MAX_EDIT_DISTANCE = 2;

    // Adds a new task and indexes it for searching
    public void addTask(Task task) {
        tasks.put(task.getTaskID(), task);
        String[] tokens = tokenizeAndNormalize(task.getTaskName());
        indexTask(task, tokens);
    }

    // Clean up the text - remove weird chars and make everything lowercase
    private String[] tokenizeAndNormalize(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .split("\\s+");
    }

    // Store words and pairs of words for better searching
    private void indexTask(Task task, String[] tokens) {
        // Save individual words
        for (String token : tokens)
            invertedIndex.computeIfAbsent(token, k -> ConcurrentHashMap.newKeySet())
                    .add(task);

        // Save pairs of words for finding exact phrases
        for (int i = 0; i < tokens.length - 1; i++) {
            String bigram = tokens[i] + " " + tokens[i + 1];
            invertedIndex.computeIfAbsent(bigram, k -> ConcurrentHashMap.newKeySet())
                    .add(task);
        }
    }

    // Main search function - tries different ways to find matches
    public List<Task> searchTasks(String query) {
        String[] queryTokens = tokenizeAndNormalize(query);
        Set<Task> results = new HashSet<>();

        // First try to match the whole phrase
        if (queryTokens.length > 1) {
            String phrase = String.join(" ", queryTokens);
            Set<Task> phraseMatches = invertedIndex.getOrDefault(phrase, Collections.emptySet());
            results.addAll(phraseMatches);
        }

        // Try matching individual words
        if (results.isEmpty())
            results.addAll(searchByTokens(queryTokens));

        // Try finding similar words (handles typos)
        if (results.isEmpty())
            results.addAll(fuzzySearch(queryTokens));

        // Sort by how well they match
        return results.stream()
                .sorted((t1, t2) -> calculateRelevance(t2, queryTokens) - calculateRelevance(t1, queryTokens))
                .collect(Collectors.toList());
    }

    // Look for exact matches and words that start the same
    private Set<Task> searchByTokens(String[] queryTokens) {
        Set<Task> results = new HashSet<>();
        for (String token : queryTokens) {
            // Exact matches first
            Set<Task> exactMatches = invertedIndex.getOrDefault(token, Collections.emptySet());
            results.addAll(exactMatches);

            // then check for words that start the same
            invertedIndex.forEach((key, tasks) -> {
                if (key.startsWith(token))
                    results.addAll(tasks);
            });
        }
        return results;
    }

    // Find similar words even if they're spelled wrong
    private Set<Task> fuzzySearch(String[] queryTokens) {
        Set<Task> results = new HashSet<>();
        for (String queryToken : queryTokens)
            for (Map.Entry<String, Set<Task>> entry : invertedIndex.entrySet())
                if (StringUtils.levenshteinDistance(queryToken, entry.getKey()) <= MAX_EDIT_DISTANCE)
                    results.addAll(entry.getValue());
        return results;
    }

    // Figure out how good each match is
    private int calculateRelevance(Task task, String[] queryTokens) {
        String[] taskTokens = tokenizeAndNormalize(task.getTaskName());
        int relevance = 0;

        for (String queryToken : queryTokens)
            for (String taskToken : taskTokens) {
                if (taskToken.equals(queryToken))
                    relevance += 2; // perfect (like u bb) match counts more
                else if (taskToken.startsWith(queryToken))
                    relevance += 1; // partial match counts a bit
            }

        return relevance;
    }

    // Try to find something close when we can't find exact matches
    public Optional<String> findClosestMatch(String query) {
        String bestMatch = null;
        int minDistance = Integer.MAX_VALUE;
        String normalizedQuery = query.toLowerCase();

        // Check full task names first
        for (Task task : tasks.values()) {
            String taskName = task.getTaskName().toLowerCase();
            int distance = StringUtils.levenshteinDistance(normalizedQuery, taskName);

            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = task.getTaskName();
            }
        }

        // then check our indexed words
        for (String indexedToken : invertedIndex.keySet()) {
            int distance = StringUtils.levenshteinDistance(normalizedQuery, indexedToken);

            if (distance < minDistance) {
                minDistance = distance;
                // Grab any task that uses this word
                Set<Task> tasksForToken = invertedIndex.get(indexedToken);
                if (!tasksForToken.isEmpty())
                    bestMatch = tasksForToken.iterator().next().getTaskName();
            }
        }

        return (minDistance <= MAX_EDIT_DISTANCE) ? Optional.ofNullable(bestMatch) : Optional.empty();
    }
}