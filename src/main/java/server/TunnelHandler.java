package server;

import tunnel.Request;
import tunnel.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TunnelHandler implements Runnable {

    private final Socket socket;

    public TunnelHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())
        ) {

            while (true) {
                Request req = Request.parseDelimitedFrom(in);
                if (req == null) break;

                String result = buildLine(req);

                Response resp = Response.newBuilder()
                        .setStatus("OK")
                        .setMessage(result)
                        .build();

                resp.writeDelimitedTo(out);
                out.flush();
            }

        } catch (IOException e) {
            System.out.println("Клиент отключился: " + e.getMessage());
        }
    }

    private String buildLine(Request req) {
        return req.getCommand() + " | " + req.getArg1() + " | " + req.getArg2();
    }
}
