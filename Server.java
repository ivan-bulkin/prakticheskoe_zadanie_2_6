package part_02.prakticheskoe_zadanie_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//Программа работает: Сервер может отправлять сообщения Клиенту и наоборот: Клиент может отправлять Сообщения Серверу
//Сообщения могут отправляться в любой момент, в любом количестве и от Сервера к Клиенту и наоборот, т.е.:
//ВЫПОЛНЕНО: Есть одна особенность, которую нужно учитывать: клиент или сервер может написать несколько сообщений подряд. Такую ситуацию необходимо корректно обработать.
//Что не сделано:
//не сделан корректный выход из программы(выскакивают исключения). Сперва хотел сделать так, чтобы при вводе \finish и у Сервера и у Клиента, обе запущенные программы закрывались, но не получилось
//Потом хотел сделать(что правильнее), чтобы при вводе \finish в Сервере закрывалось и серверное и клиентское приложение, а при вводе \finish в Клиенте закрывалось только серверное приложение
//Также не сделана проверка на то, что Сервер запущен. Т.е., если Сервер не запущен и мы запускаем Клиент, то лезут исключения
//Пожалуйста, не обращайте внимание на заккоментированный код, этот код появлялся во время разработки программы.
//Я не стал удалять заккоментированный код, чтобы при следующем подобном программировании не допускать аналогичные ошибки и понимать, что работает, а что нет
// и чтобы потом доработать код и сделать то, что не сделано
public class Server {

    private static String messageFromClient;
    private static String messageToClient;

    public static void main(String[] args) throws InterruptedException, IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Создали серверный сокет.");
        System.out.println("Ожидаем подключение клиента.");
        Socket socket = serverSocket.accept();//ожидаем подключение, дальше этой строки программа не пойдёт, пока клиент не подключится
        System.out.println("Клиент подключился.");
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
/*        Thread threadServer = new Thread() {
            @Override
            //переопределяем метод run
            public void run() {
                try {
                    messageFromClient = dataInputStream.readUTF();
                    System.out.println("\nСервер получил сообщение от клиента: " + messageFromClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };*/
        Thread threadServer = new Thread() {
            @Override
            //переопределяем метод run
            public void run() {
                do {
                    try {
                        messageFromClient = dataInputStream.readUTF();
                        System.out.println("\nСервер получил сообщение от клиента: " + messageFromClient);
                        System.out.print("Сервер вводит сообщение здесь: ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while (!isInterrupted());
            }
        };
        threadServer.start();
        do {
//            messageFromClient = dataInputStream.readUTF();
//            System.out.println("Сервер получил сообщение от клиента: " + messageFromClient);
            System.out.print("Сервер вводит сообщение здесь: ");
            messageToClient = scanner.nextLine();
            if (messageToClient != "") {//не отправляем сообщение, если ничего не введено(просто нажат Enter)
                dataOutputStream.writeUTF(messageToClient);
                dataOutputStream.flush();//отправляем, очистить буфер отправки(выдавить буфер)
            }
        } while (!"\\finishServer".equals(messageToClient));
        threadServer.interrupt();
//            System.out.print("АГА Сервер: ");
//            messageToClient = scanner.nextLine();
        socket.close();
        scanner.close();//Необходимо закрыть объект scanner
//            System.exit(0);
    }
}
