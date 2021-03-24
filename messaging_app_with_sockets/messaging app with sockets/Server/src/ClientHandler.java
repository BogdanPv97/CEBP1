import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class ClientHandler extends Thread {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final BlockingQueue<Message> messages = new ArrayBlockingQueue<>(10);
    private final Gson gson;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.gson = new Gson();
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String name;
        try {
            output.print(
                    "To get topic list: get-topics\n" +
                            "To get client list: get-clients\n" +
                            "To add a new topic: add-topic\n" +
                            "To subscribe to a topic: subscribe-to-topic\n" +
                            "To send a DM: message/recipient/message-text\n" +
                            "To add to topic: topic/topic-name/time-in-seconds/message-text\n" +
                            "To close the client: exit\n"
            );
            output.println("Tell me your name: ");
            while (true) {
                String myName = input.readLine();
                if (Server.names.get(myName) != null) {
                    output.println("Name taken. Try again:");
                    continue;
                } else {
                    Server.names.put(myName, socket.hashCode());
                    output.println("Enter commands:");
                    break;
                }
            }
            while (true) {
                try {
                    Message message = messages.poll(1, TimeUnit.SECONDS);
                    if (message != null) {
                        output.println(message.getAuthor() + ": " + message.getMessage());
                        System.out.println(message.getAuthor() + ": " + message.getMessage());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!input.ready()) {
                    continue;
                }

                String echoString;
                echoString = input.readLine();
                System.out.println("Received client input: " + echoString);

                if (echoString.equals("exit")) {
                    output.println("exit");
                    break;
                }

                if (echoString.equals("get-topics")) {
                    output.println("Topics: ");
                    for (Map.Entry<String, Vector<String>> topic : Server.topics.entrySet()) {
                        output.println(topic.getKey());
                    }
                    output.println("Enter new command: ");
                    continue;
                }

                if (echoString.equals("get-clients")) {
                    output.println("Clients: ");
                    for (HashMap.Entry<String, Integer> name_client : Server.names.entrySet()) {
                        output.println(name_client.getKey());
                    }
                    output.println("Enter new command: ");
                    continue;
                }

                if (echoString.equals("subscribe-to-topic")) {
                    output.println("Tell me the name of the topic: ");
                    String topic = input.readLine();
                    String myName = "";
                    for (HashMap.Entry<String, Integer> entry : Server.names.entrySet()) {
                        if (entry.getValue().equals(socket.hashCode())) {
                            myName = entry.getKey();
                        }
                    }
                    Server.topics.get(topic).add(myName);///---------------------------aici seeems to be the last issue
                    output.println("Enter new command: ");
                    continue;
                }

                if (echoString.equals("add-topic")) {
                    output.println("Tell me the name of the topic: ");
                    String topic = input.readLine();
                    Server.topics.put(topic, new Vector<>());
                    String myName = "";
                    for (HashMap.Entry<String, Integer> entry : Server.names.entrySet()) {
                        if (entry.getValue().equals(socket.hashCode())) {
                            myName = entry.getKey();
                        }
                    }
                    Server.topics.get(topic).add(myName);
                    output.println("Enter new command: ");
                    continue;
                }

                Message obj = gson.fromJson(echoString, Message.class);
                if (obj.getType().equals("message")) {
                    DirectMessage dm = gson.fromJson(echoString, DirectMessage.class);
                    //get head messages
                    if (dm.getRecipient() != null) {
                        System.out.println(dm.getRecipient());
                        Integer hash = Server.names.get(dm.getRecipient());
                        System.out.println(hash);
                        try {
                            Server.clients.get(hash).messages.put(dm);
                            System.out.println("other " + Server.clients.get(hash).messages);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        output.println("Need to have a recipient!");
                        output.println("DM format: message/recipient/text-here");
                    }
                } else {
                    Topic topicRaw = gson.fromJson(echoString, Topic.class);
                    String topic = topicRaw.getTopic();
                    for (String name_client : Server.topics.get(topic)) {
                        Integer hash = Server.names.get(name_client);
                        try {
                            Server.clients.get(hash).messages.put(topicRaw);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}