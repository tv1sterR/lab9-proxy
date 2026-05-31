package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tunnel.Request;
import tunnel.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainController {

    @FXML
    private ComboBox<String> connectionTypeBox;

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;

    private Socket socket;
    private BufferedInputStream binIn;
    private BufferedOutputStream binOut;

    @FXML
    public void initialize() {
        connectionTypeBox.getItems().addAll("TCP", "TUNNEL", "UDP");
    }

    @FXML
    private void onConnect() {
        try {
            String type = connectionTypeBox.getValue();
            if (type == null) {
                outputArea.appendText("Выберите тип соединения\n");
                return;
            }

            String ip = ipField.getText().trim();
            if (ip.isEmpty()) {
                outputArea.appendText("Введите IP сервера\n");
                return;
            }

            int port;
            try {
                port = Integer.parseInt(portField.getText().trim());
            } catch (Exception e) {
                outputArea.appendText("Некорректный порт\n");
                return;
            }

            switch (type) {
                case "TCP", "TUNNEL" -> socket = new Socket(ip, port);
                case "UDP" -> {
                    outputArea.appendText("UDP пока не реализован\n");
                    return;
                }
            }

            binIn = new BufferedInputStream(socket.getInputStream());
            binOut = new BufferedOutputStream(socket.getOutputStream());

            outputArea.appendText("Подключено: " + ip + ":" + port + " (" + type + ")\n");

        } catch (IOException e) {
            outputArea.appendText("Ошибка подключения: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void onSendClicked() {
        if (socket == null || socket.isClosed()) {
            outputArea.appendText("Нет подключения к серверу\n");
            return;
        }

        try {
            String text = inputField.getText();

            Request req = Request.newBuilder()
                    .setCommand("ECHO")
                    .setArg1(text)
                    .build();

            req.writeDelimitedTo(binOut);
            binOut.flush();

            Response resp = Response.parseDelimitedFrom(binIn);

            outputArea.appendText("Ответ: " + resp.getMessage() + "\n");

        } catch (IOException e) {
            outputArea.appendText("Ошибка при отправке/получении: " + e.getMessage() + "\n");
        }
    }
}
