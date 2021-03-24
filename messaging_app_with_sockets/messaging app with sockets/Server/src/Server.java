import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server {
    public final static int max_user = 100;
    public static PrintWriter[] printers = new PrintWriter[max_user];
    public static ConcurrentMap<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, Integer> names = new ConcurrentHashMap<>();
    public static ConcurrentMap<String,Vector<String>> topics = new ConcurrentHashMap<>();

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(5000)) {
            while(true) {
                Socket socket = serverSocket.accept(); //blocks until a client is connected
                System.out.println(socket.hashCode());
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.put(socket.hashCode(),clientHandler);
                System.out.println("Client connected");
                clientHandler.start();
            }

        } catch(IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }
}
