import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TESTCLIENT1 {
    public static void main(String[] args) throws InterruptedException {

// запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента      
        try(Socket socket = new Socket("localhost", 3345);
            Scanner input = new Scanner(System.in);
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream());)
        {
            System.out.println("Client connected to socket.\n");
            System.out.print("Client writing channel = oos & reading channel = ois initialized.\n");

            while(!socket.isOutputShutdown()){// проверяем живой ли канал и работаем если живой
                System.out.print("Input: "); // ждём консоли клиента на предмет появления в ней данных
                String clientCommand = input.nextLine();
                    oos.writeUTF(clientCommand);// пишем данные с консоли в канал сокета для сервера
                    oos.flush();
                    System.out.println("Client sent message " + "'" + clientCommand + "'" + " to server.");
                    Thread.sleep(1000);
// ждём чтобы сервер успел прочесть сообщение из сокета и ответить
// проверяем условие выхода из соединения
                    if(clientCommand.equalsIgnoreCase("kill")){// если условие выхода достигнуто разъединяемся
                        System.out.println("Client kill connections");
                        Thread.sleep(2000);
// смотрим что нам ответил сервер на последок перед закрытием ресурсов
                            System.out.println("reading...1");
                            System.out.println(ois.readUTF() + " — Answer");
// после предварительных приготовлений выходим из цикла записи чтения
                        break;
                    }
                    String check=ois.readUTF();
                    if(check.equals("ERROR")){
                        System.out.println("Input ERROR");
                        continue;
                    }
// если условие разъединения не достигнуто продолжаем работу            
                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);
// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль                       
                        System.out.println("reading...2");
                       System.out.println(check + " — Answer " );

            }
// на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}