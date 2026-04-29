package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.net.Socket;

public class MainController {

    @FXML private ChoiceBox<String> levelChoice;
    @FXML private TextArea outputArea;
    @FXML private TextField inputField;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    @FXML
    public void initialize() {
        levelChoice.getItems().addAll("0 — прямое", "1 — прокси");
        levelChoice.getSelectionModel().select(0);
    }

    @FXML
    public void onConnect() {
        try {
            int level = levelChoice.getSelectionModel().getSelectedIndex();

            if (level == 0) {
                socket = new Socket("localhost", 5555);
            } else {
                socket = new Socket("localhost", 5556);
            }

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            outputArea.appendText("Подключено.\n");

        } catch (Exception e) {
            outputArea.appendText("Ошибка подключения: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void onDisconnect() {
        try {
            if (socket != null) socket.close();
            outputArea.appendText("Отключено.\n");
        } catch (IOException e) {
            outputArea.appendText("Ошибка: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void onSend() {
        try {
            String cmd = inputField.getText();
            inputField.clear();

            if (out == null) {
                outputArea.appendText("Нет подключения.\n");
                return;
            }

            out.println(cmd);
            String resp = in.readLine();

            outputArea.appendText("> " + cmd + "\n");
            outputArea.appendText(resp + "\n");

        } catch (Exception e) {
            outputArea.appendText("Ошибка отправки: " + e.getMessage() + "\n");
        }
    }
}
