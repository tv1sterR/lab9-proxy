package proxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {

    private final int listenPort;
    private final String serverHost;
    private final int serverPort;

    public ProxyServer(int listenPort, String serverHost, int serverPort) {
        this.listenPort = listenPort;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        try (ServerSocket ss = new ServerSocket(listenPort)) {
            System.out.println("Proxy on " + listenPort);
            while (true) {
                Socket client = ss.accept();
                new Thread(() -> handle(client)).start();
            }
        }
    }

    private void handle(Socket client) {
        try (client;
             Socket server = new Socket(serverHost, serverPort)) {

            Thread t1 = new Thread(() -> pipe(client, server));
            Thread t2 = new Thread(() -> pipe(server, client));
            t1.start();
            t2.start();
            t1.join();
            t2.join();

        } catch (Exception ignored) {}
    }

    private void pipe(Socket inSock, Socket outSock) {
        try (InputStream in = inSock.getInputStream();
             OutputStream out = outSock.getOutputStream()) {
            in.transferTo(out);
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) throws IOException {
        new ProxyServer(5556, "localhost", 5555).start();
    }
}
