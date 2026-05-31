package proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TextProxyServer {

    public static void main(String[] args) throws Exception {
        ServerSocket proxy = new ServerSocket(5556); // уровень 1
        System.out.println("Text proxy on 5556 → 5555");
        while (true) {
            Socket client = proxy.accept();
            new Thread(new TextProxyWorker(client)).start();
        }
    }

    private static class TextProxyWorker implements Runnable {
        private final Socket client;

        TextProxyWorker(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (Socket server = new Socket("127.0.0.1", 5555)) {
                Thread t1 = new Thread(() -> forward(client, server));
                Thread t2 = new Thread(() -> forward(server, client));
                t1.start();
                t2.start();
                t1.join();
                t2.join();
            } catch (Exception ignored) {}
        }

        private void forward(Socket from, Socket to) {
            try {
                from.getInputStream().transferTo(to.getOutputStream());
            } catch (IOException ignored) {}
        }
    }
}
