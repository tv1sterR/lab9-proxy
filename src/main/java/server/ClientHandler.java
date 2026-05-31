package server;

import server.model.Storage;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final CommandProcessor processor;

    public ClientHandler(Socket socket, Storage storage) {
        this.socket = socket;
        this.processor = new CommandProcessor(storage);
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                String resp = processor.handle(line.trim());
                out.println(resp);
            }
        } catch (IOException e) {
            System.out.println("Text client disconnected: " + e.getMessage());
        }
    }
}
