package server;

import index.InvertedIndex;

import java.io.*;
import java.net.Socket;

public class ConnectionProcessor {
    private final Socket connection;
    private final InvertedIndex index;
    private final BufferedReader in;
    private final BufferedWriter out;

    public ConnectionProcessor(Socket connection, InvertedIndex index) {
        this.connection = connection;
        this.index = index;

        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void process() {
        try {
            sendMessage("CONN-ACPT  processed by" + Thread.currentThread().getName());

            var incomingMessage = "";
            while (true) {
                incomingMessage = acceptMessage();

                if (incomingMessage.equals("/quit")) break;

                System.out.println(incomingMessage);
                sendMessage(incomingMessage);
            }

            sendMessage("CONN-CLOSED");
        } catch (RuntimeException e) {
            System.out.println("PORT: " + connection.getPort() + "\nERROR: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String acceptMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
