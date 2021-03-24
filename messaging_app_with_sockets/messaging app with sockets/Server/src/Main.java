import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    public final static int max_user = 100;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}