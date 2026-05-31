package server;

import server.model.Storage;

import java.net.ServerSocket;
import java.net.Socket;

public class ChallengeServer {

    public static void main(String[] args) throws Exception {
        Storage storage = new Storage();

        ServerSocket textServer = new ServerSocket(5555); // уровень 0
        ServerSocket tunnelServer = new ServerSocket(5557); // уровень 2

        System.out.println("Text server on 5555, tunnel server on 5557");

        while (true) {
            Socket textClient = textServer.accept();
            new Thread(new ClientHandler(textClient, storage)).start();

            Socket tunnelClient = tunnelServer.accept();
            new Thread(new TunnelHandler(tunnelClient)).start();
        }
    }
}
