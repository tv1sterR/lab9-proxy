package server;

import server.model.Storage;

import java.net.ServerSocket;
import java.net.Socket;

public class ChallengeServer {

    private final int port;
    private final Storage storage = new Storage();

    public ChallengeServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket s = ss.accept();
                new Thread(new ClientHandler(s, storage)).start();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ChallengeServer(5555).start();
    }
}
