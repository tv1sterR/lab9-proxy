package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConsoleClient {
    public static void main(String[] args) throws Exception {
        try (Socket s = new Socket("localhost", 5555);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected. Enter commands:");
            while (true) {
                String cmd = sc.nextLine();
                out.println(cmd);
                String resp = in.readLine();
                System.out.println("SERVER: " + resp);
            }
        }
    }
}
