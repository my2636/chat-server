import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static ExecutorService threadPool = Executors.newFixedThreadPool(20);

    void listen(int port) throws IOException {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                System.out.println("Новое соединение");
                threadPool.submit(() -> getConnection(socket));
            }
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
        String message = in.readLine();

    }
}