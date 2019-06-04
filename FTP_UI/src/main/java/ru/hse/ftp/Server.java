package ru.hse.ftp;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/** Implementation of server */
@SuppressWarnings("WeakerAccess")
public class Server {
    public static final int PORT = 10594;
    private static final int BUFFER_SIZE = 1024;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        ServerSocket ss = new ServerSocket(PORT);

        while (true) {
            Socket socket = ss.accept();
            new Thread(() -> {
                try (var in = new DataInputStream(socket.getInputStream());
                     var out = new DataOutputStream(socket.getOutputStream())) {
                    while (!Thread.interrupted()) {
                        var query = in.readUTF();
                        if (!rightFormat(query)) {
                            socket.close();
                            break;
                        } else {
                            threadPool.submit(() -> {
                                try {
                                    processQuery(query, out);
                                    out.flush();
                                } catch (IOException e) {
                                    tryToCloseSocket(socket);
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    tryToCloseSocket(socket);
                }
            }).start();
        }
    }

    /** Tries to close socket after exception */
    private static void tryToCloseSocket(@NotNull Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Checks that request format is correct */
    private static boolean rightFormat(@NotNull String request) {
        var lines = request.split(" ");

        return lines.length == 2 && (lines[0].equals("1") || lines[0].equals("2"));
    }

    /** Processes query */
    private static void processQuery(@NotNull String query, @NotNull DataOutputStream out) throws IOException {
        var lines = query.split(" ");

        if (lines[0].equals("1")) {
            answerList(lines[1], out);
        } else {
            answerGet(lines[1], out);
        }
    }

    /** Processes get query */
    private static void answerGet(@NotNull String path, @NotNull DataOutputStream out) throws IOException {
        var file = new File(path);
        if (!file.exists() || !file.isFile()) {
            out.writeLong(-1);
        }

        try (var input = new BufferedInputStream(new FileInputStream(file))) {
            var buffer = new byte[BUFFER_SIZE];
            int symbolsRead;

            out.writeLong(file.length());
            while ((symbolsRead = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, symbolsRead);
            }
        }
    }

    /** Processes list query */
    private static void answerList(@NotNull String path, @NotNull DataOutputStream out) throws IOException {
        var directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            out.writeInt(-1);
        }

        var files = directory.listFiles();
        if (files == null) {
            out.writeInt(0);
            return;
        }

        out.writeInt(files.length);
        for (var file : Arrays.stream(files).sorted().collect(Collectors.toList())) {
            out.writeUTF(file.getName());
            out.writeBoolean(file.isDirectory());
        }
    }
}
