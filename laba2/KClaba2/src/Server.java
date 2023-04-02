import java.net.*;
import java.io.*;
public class Server{
    public static void main(String args[]) {
        DatagramSocket aSocket = null;
        try{
            // создаём сокет: порт 2100
            aSocket = new DatagramSocket(2100);
            // резервируем буфер под клиентское сообщение
            byte [] buffer = new byte[1000];
            while(true){ // бесконечный цикл
                // резервируем дейтаграмму под пакет клиента
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                // принимаем пакет клиента
                aSocket.receive(request);
                // создаём ответную дейтаграмму
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());
                // отправляем ответ клиенту
                aSocket.send(reply);
            }
        }
        catch (SocketException e) {
            // обрабатываем ошибку создания сокета
            System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e) {
            // обрабатываем ошибку передачи пакета
            System.out.println("IO: " + e.getMessage());
        }
        finally {
            // закрываем сокет
            if(aSocket != null)
                aSocket.close();
        }
    }
}