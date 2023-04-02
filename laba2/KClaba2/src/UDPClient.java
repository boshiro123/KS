import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class UDPClient{
    /* Порт сервера, к которому собирается
  подключиться клиентский сокет */
    public final static int SERVICE_PORT = 50001;

    public static void main(String[] args) throws IOException{
        Scanner input = new Scanner(System.in);
        try{
      /* Создайте экземпляр клиентского сокета.
Нет необходимости в привязке к определенному порту */
            DatagramSocket clientSocket = new DatagramSocket();

            // Получите IP-адрес сервера
            InetAddress IPAddress = InetAddress.getByName("localhost");

            // Создайте соответствующие буферы

      /* Преобразуйте данные в байты
       и разместите в буферах */
            System.out.print("Input String: ");
            String sentence = input.nextLine();
            byte[] sendingDataBuffer = new byte[sentence.length()];
            byte[] receivingDataBuffer = new byte[sentence.length()];
            sendingDataBuffer = sentence.getBytes();//!!!!!!!!!!!!

            // Создайте UDP-пакет
            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer,sendingDataBuffer.length,IPAddress, SERVICE_PORT);

            // Отправьте UDP-пакет серверу
            clientSocket.send(sendingPacket);

            // Получите ответ от сервера, т.е. предложение из заглавных букв
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);

            // Выведите на экране полученные данные
            String receivedData = new String(receivingPacket.getData());
            String nal = String.valueOf(receivedData.toCharArray()[sentence.length()-1]);
            System.out.println("Sent from the server: " +  receivedData.replaceAll(nal,""));

            // Закройте соединение с сервером через сокет
            clientSocket.close();
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
    }
}