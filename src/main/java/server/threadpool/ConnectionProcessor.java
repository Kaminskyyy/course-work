package server.threadpool;

import index.InvertedIndex;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;

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
            sendMessage("CONN-ACPT");

            var incomingMessage = "";
            while (true) {
                incomingMessage = acceptMessage();

                if (incomingMessage.equals("/quit")) break;

                var searchRequest = incomingMessage.toLowerCase().split(" ");
                var result = processRequest(searchRequest);

                sendMessage(result);
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
            out.write(message + "/end");
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

    private String processRequest(String[] words) {
        var files = new HashSet<>(index.get(words[0]).keySet());

        for (var i = 1; i < words.length; i++) {
            files.retainAll(index.get(words[i]).keySet());
        }

        var result = "";
        for (var file: files) {
            result += (file + " ");
        }

        return result.stripIndent();
    }
}
