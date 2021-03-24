import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class ClientRead extends Thread{
    private Socket socket;
    private BufferedReader echoes;
    public ClientRead(Socket socket){
        this.socket = socket;
        try {
            echoes = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try
        {
            String fromServer = new String();
            while (!fromServer.equals("exit")){
                if (echoes.ready()) {
                    fromServer = echoes.readLine();
                    System.out.println(fromServer);
                }
            }
        } catch(SocketTimeoutException e) {
            System.out.println("The socket timed out");
        } catch (IOException e) {
            System.out.println("Client closed: " + e.getMessage());
        }
    }
}