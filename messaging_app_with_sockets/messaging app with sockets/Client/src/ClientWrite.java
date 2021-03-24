import com.google.gson.Gson;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientWrite extends Thread {
    private PrintWriter stringToEcho;

    public ClientWrite(PrintWriter pr) {
        stringToEcho = pr;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String echoString = "";
        Gson gson = new Gson();

        String name = scanner.nextLine();
        stringToEcho.println(name);

        while (!echoString.equals("exit")) {
            echoString = scanner.nextLine();

            if (echoString.equals("exit")) {
                stringToEcho.println(echoString);
                break;
            }

            if (echoString.equals("get-topics")) {
                stringToEcho.println(echoString);
                continue;
            }

            if (echoString.equals("get-clients")) {
                stringToEcho.println(echoString);
                continue;
            }

            if (echoString.equals("subscribe-to-topic")) {
                stringToEcho.println(echoString);
                echoString = scanner.nextLine();
                stringToEcho.println(echoString);
                continue;
            }

            if (echoString.equals("add-topic")) {
                stringToEcho.println(echoString);
                echoString = scanner.nextLine();
                stringToEcho.println(echoString);
                continue;
            }

            String[] type = echoString.split("/", 2);
            if (type[0].equals("message")) {
                String[] info = type[1].split("/", 2);
                DirectMessage dm = new DirectMessage(name, type[0], info[0], info[1]);
                echoString = gson.toJson(dm);
                System.out.println(echoString);
                stringToEcho.println(echoString);
                System.out.println("Enter command: ");
            } else if (type[0].equals("topic")) {
                String[] info = type[1].split("/", 3);
                Topic topic = new Topic(name, type[0], info[0], Integer.parseInt(info[1]), info[2]);
                echoString = gson.toJson(topic);
                stringToEcho.println(echoString);
                System.out.println("Enter command: ");
            } else {
                System.out.println("unknown command");
                break;
            }
        }
    }
}