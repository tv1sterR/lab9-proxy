package server;

import server.model.Storage;

public class CommandProcessor {

    private final Storage storage;
    private String currentUser;

    public CommandProcessor(Storage storage) {
        this.storage = storage;
    }

    public String handle(String line) {
        if (line == null || line.isBlank()) return "ERROR|Пустая команда";

        String[] parts = line.split("\\|", 3);
        String cmd = parts[0].trim();

        switch (cmd) {
            case "REGISTER": {
                if (parts.length < 2) return "ERROR|Нет имени";
                String name = parts[1].trim();
                boolean ok = storage.register(name);
                if (ok) {
                    currentUser = name;
                    return "OK|Зарегистрирован как " + name;
                } else {
                    return "ERROR|Имя уже занято";
                }
            }

            case "CHALLENGE": {
                if (currentUser == null) return "ERROR|Сначала REGISTER";
                if (parts.length < 3) return "ERROR|Нужен получатель и текст";
                String to = parts[1].trim();
                String text = parts[2].trim();
                if (!storage.isRegistered(to)) {
                    return "ERROR|Получатель не зарегистрирован";
                }
                storage.addChallenge(to, text);
                return "OK|Задание отправлено";
            }

            case "INBOX": {
                if (currentUser == null) return "ERROR|Сначала REGISTER";
                var list = storage.getInbox(currentUser);
                if (list.isEmpty()) return "OK|Нет заданий";
                String joined = String.join(" | ", list);
                return "OK|Ваши задания: " + joined;
            }

            case "DONE": {
                if (currentUser == null) return "ERROR|Сначала REGISTER";
                boolean ok = storage.markDone(currentUser);
                if (ok) return "OK|Задания отмечены выполненными";
                else return "OK|Нет активных заданий";
            }

            case "REVEAL": {
                return "OK|Авторы заданий не раскрываются в этой игре";
            }

            default:
                return "ERROR|Неизвестная команда";
        }
    }
}
