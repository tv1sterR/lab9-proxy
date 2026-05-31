package proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TunnelProxyServer {

    public static void main(String[] args) throws Exception {
        ServerSocket proxy = new ServerSocket(5558); // уровень 3
        System.out.println("Tunnel proxy on 5558 → 5557");
        while (true) {
            Socket client = proxy.accept();
            new Thread(new TunnelProxyWorker(client)).start();
        }
    }

    private static class TunnelProxyWorker implements Runnable {
        private final Socket client;

        TunnelProxyWorker(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (Socket server = new Socket("127.0.0.1", 5557)) {
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
