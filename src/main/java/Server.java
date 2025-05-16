import com.jcraft.jsch.JSch;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static ExecutorService threadPool = Executors.newFixedThreadPool(20);

    void listen(int port) throws IOException {
        try (final var serverSocket = new ServerSocket(port);) {
            while (true) {
                final var socket = serverSocket.accept();
                System.out.println("Новое соединение");
                threadPool.submit(() -> getConnection(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getConnection(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            processConnection(in, out);
        } catch (IOException e) {
            System.err.println("Ошибка обработки подключения: " + e.getMessage());
        }
    }

    private void processConnection(BufferedReader in, BufferedOutputStream out) throws IOException {
        JSch jsch = new JSch();
        String connstr = JOptionPane.showInputDialog("Enter username:password@hostname", System.getProperty("user.name")+":<pass>"+"@localhost");

        String message = in.readLine();
        String filePath = "./group/messages.txt";
        System.out.println(message);
        FileWriter writer = new FileWriter(filePath, true);
        writer.write(URLDecoder.decode(message, StandardCharsets.UTF_8) + "\n");
        Files.copy(Path.of(filePath), out);
        out.flush();

    }
}