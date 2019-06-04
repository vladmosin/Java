package ru.hse.ftp;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static ru.hse.ftp.Server.PORT;

/** Implementation of client. Possible operations are get file and get list of files */
@SuppressWarnings("WeakerAccess")
public class Client {
    /** Output stream to get server answers */
    private DataOutputStream out;

    /** Input stream to ask server */
    private DataInputStream in;

    /** Size of buffer */
    private static final int BUFFER_SIZE = 1024;

    public Client(@NotNull String host, int port) throws IOException {
        var ip = InetAddress.getByName(host);
        var socket = new Socket(ip, port);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public Client() throws IOException {
        this("localhost", PORT);
    }

    public Client(int port) throws IOException {
        this("localhost", port);
    }

    /** Executes list query
     * @param path to directory
     * */
    public String executeList(@NotNull String path) throws IOException {
        out.writeUTF("1 " + path);
        out.flush();

        int size = in.readInt();
        var stringBuilder = new StringBuilder();

        stringBuilder.append(size);

        for (int i = 0; i < size; i++) {
            stringBuilder.append(" ");
            stringBuilder.append(in.readUTF());
            stringBuilder.append(in.readBoolean() ? " 1" : " 0");
        }

        return stringBuilder.toString();
    }

    /** Executes get query
     * @param path path to file
     * */
    public String executeGet(@NotNull String path) throws IOException {
        out.writeUTF("2 " + path);
        out.flush();

        long size = in.readLong();
        if (size == 0) {
            return "0";
        }

        var stringBuilder = new StringBuilder(size + " ");
        var buffer = new byte[BUFFER_SIZE];

        for (int i = 0; i < size; i += BUFFER_SIZE) {
            int symbolsToRead = Math.min((int)size - i, BUFFER_SIZE);

            symbolsToRead = in.read(buffer, 0, symbolsToRead);
            stringBuilder.append(new String(buffer, 0, symbolsToRead));
        }

        return stringBuilder.toString();
    }

    /** Starts client in console */
    private void start() {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Usage:");
        System.out.println("<exit> - to exit the program");
        System.out.println("<get> <path> - to get file");
        System.out.println("<list> <path> - to get list of files in directory");

        while (true) {
            try {
                var request = reader.readLine();
                if (request.equals("exit")) {
                    return;
                } else {
                    var args = request.split(" ");
                    if (args.length != 2) {
                        System.out.println("Bad request");
                    } else if (args[0].equals("get")) {
                        System.out.println(executeGet(args[1]));
                    } else if (args[0].equals("list")) {
                        System.out.println(executeList(args[1]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Executes new client */
    public static void main(String[] args) throws IOException {
        (new Client()).start();
    }
}
