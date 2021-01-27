package part_02.prakticheskoe_zadanie_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {

    private static String inputString;
    private static String messageFromServer;

    public static void main(String[] args) throws InterruptedException, IOException {
//        String id = UUID.randomUUID().toString();//Можно создать очень-супер уникальный id клиента
        Socket socket = new Socket("localhost", 8081);
        Scanner scanner = new Scanner(System.in);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
/*        Thread threadClient = new Thread() {
            @Override
//переопределяем метод run
            public void run() {
                try {

                    messageFromServer = dataInputStream.readUTF();
                    System.out.println("\nСообщение от сервера: " + messageFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };*/
//создаём новый(отдельный) поток
        Thread threadClient = new Thread() {
            @Override
            //переопределяем метод run
            public void run() {
                do {
                    try {
                        messageFromServer = dataInputStream.readUTF();
                        System.out.println("\nСообщение от сервера: " + messageFromServer);
/*                        if ("\\finish".equals(messageFromServer)) {
                            System.out.print("Выходим тут");
//                            socket.close();
                        }*/
                        System.out.print("Клиент вводит сообщение здесь: ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (!isInterrupted());
//                } while ((!isInterrupted()) || ("\\finish".equals(messageFromServer)));
//                System.out.print("Выходим");
            }
        };
        threadClient.start();
//        System.out.println(messageFromServer);
        do {
            System.out.print("Клиент вводит сообщение здесь: ");
            inputString = scanner.nextLine();
            if (inputString != "") {//не отправляем сообщение, если ничего не введено(просто нажат Enter)
                dataOutputStream.writeUTF(inputString);
                dataOutputStream.flush();//отправляем, очистить буфер отправки(выдавить буфер)
            }
//            } while (!"\\finish".equals(inputString));
        } while (!"\\finishClient".equals(inputString));
        socket.close();
        scanner.close();//Необходимо закрыть объект scanner
//        doClientExit(socket, threadClient);
    }

/*    private static void doClientExit(Socket socket, Thread threadClient) throws IOException {
        threadClient.interrupt();
        socket.close();
        scanner.close();//Необходимо закрыть объект scanner
    }*/
}



