package server.model;

import java.util.*;

public class Storage {

    private final Set<String> users = new HashSet<>();
    private final Map<String, List<String>> inbox = new HashMap<>();
    private final Map<String, List<String>> done = new HashMap<>();

    public synchronized boolean register(String name) {
        return users.add(name);
    }

    public synchronized boolean isRegistered(String name) {
        return users.contains(name);
    }

    public synchronized void addChallenge(String to, String text) {
        inbox.computeIfAbsent(to, k -> new ArrayList<>()).add(text);
    }

    public synchronized List<String> getInbox(String user) {
        return inbox.getOrDefault(user, Collections.emptyList());
    }

    public synchronized void clearInbox(String user) {
        inbox.remove(user);
    }

    // пометить все задания из inbox как выполненные
    public synchronized boolean markDone(String user) {
        List<String> tasks = inbox.get(user);
        if (tasks == null || tasks.isEmpty()) {
            return false;
        }
        done.computeIfAbsent(user, k -> new ArrayList<>()).addAll(tasks);
        inbox.remove(user);
        return true;
    }

    public synchronized List<String> getDone(String user) {
        return done.getOrDefault(user, Collections.emptyList());
    }
}
