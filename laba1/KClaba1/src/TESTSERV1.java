import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TESTSERV1 {
    public static void main(String[] args) throws InterruptedException {

        try (ServerSocket server= new ServerSocket(3345)){//  стартуем сервер на порту 3345
            Socket client = server.accept();// становимся в ожидание подключения к сокету под именем - "client" на серверной стороне

            System.out.println("Connection accepted.");// после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением

// инициируем каналы для  общения в сокете, для сервера
// канал записи в сокет
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");

            // канал чтения из сокета
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

// начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт                
            while(!client.isClosed()){

                System.out.println("Server reading from channel\n");

// сервер ждёт в канале чтения (inputstream) получения данных клиента               
                String entry = in.readUTF();
                String operation;

// после получения данных считывает их              
                System.out.println("READ from client message - "+"'"+entry+"'");
// и выводит в консоль              
                System.out.println("Server try writing to channel");

// инициализация проверки условия продолжения работы с клиентом по этому сокету по кодовому слову - kill
                if(entry.equalsIgnoreCase("kill")){
                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - Killing");
                    out.flush();
                    Thread.sleep(3000);
                    break;
                }
                try {
                    if (entry.length() == 6) {
                        System.out.println("Correct input");
                        char[] numb = entry.toCharArray();
                        int first,second;
                        first=Character.getNumericValue(numb[0])+Character.getNumericValue(numb[1])+Character.getNumericValue(numb[2]);
                        second=Character.getNumericValue(numb[3])+Character.getNumericValue(numb[4])+Character.getNumericValue(numb[5]);
                        if(first==second)out.writeUTF("Happy number");
                        else {
                            out.writeUTF("Not happy number");
                        }
                    } else {
                        out.writeUTF("ERROR");
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("NumberFormatException");
                }
                // если условие окончания работы не верно - продолжаем работу - отправляем эхо-ответ  обратно клиенту
                System.out.println("Server Wrote message to client.");
// освобождаем буфер сетевых сообщений (по умолчанию сообщение не сразу отправляется в сеть, а сначала накапливается в специальном буфере сообщений, размер которого определяется конкретными настройками в системе, а метод  - flush() отправляет сообщение не дожидаясь наполнения буфера согласно настройкам системы
                out.flush();
            }

// если условие выхода - верно выключаем соединения             
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");
            // закрываем сначала каналы сокета !
            in.close();
            out.close();
            // потом закрываем сам сокет общения на стороне сервера!
            client.close();
            // потом закрываем сокет сервера который создаёт сокеты общения
            // хотя при многопоточном применении его закрывать не нужно
            // для возможности поставить этот серверный сокет обратно в ожидание нового подключения
            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}