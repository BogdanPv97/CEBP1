import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        try
        {
            Socket socket = new Socket("localhost", 5000);

            PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);
            new ClientRead(socket).start();
            new ClientWrite(stringToEcho).start();

        } catch(SocketTimeoutException e) {
            System.out.println("The socket timed out");
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}