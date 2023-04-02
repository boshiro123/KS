import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer{
    // Серверный UDP-сокет запущен на этом порту
    public final static int SERVICE_PORT=50001;

    public static void main(String[] args) throws IOException{
        try{
            // Создайте новый экземпляр DatagramSocket, чтобы получать ответы от клиента
            DatagramSocket serverSocket = new DatagramSocket(SERVICE_PORT);
      /* Создайте буферы для хранения отправляемых и получаемых данных.
Они временно хранят данные в случае задержек связи */
            byte[] receivingDataBuffer = new byte[1024];
            byte[] sendingDataBuffer = new byte[1024];

            /* Создайте экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for a client to connect...");

            // Получите данные от клиента и сохраните их в inputPacket
            serverSocket.receive(inputPacket);

            // Выведите на экран отправленные клиентом данные
            String receivedData = new String(inputPacket.getData());
            System.out.println(receivedData);
            System.out.print("Sent from the client: ");
            int i=0;
            int firstInd=0;
            char[] str = receivedData.toCharArray();
            char nal = receivedData.toCharArray()[1023];
            while(str[i]!=nal){
                i++;
                if(str[i]==' ' && firstInd==0)firstInd=i;
            }
            receivedData=receivedData.substring(0,i);
            System.out.println(receivedData);
            System.out.println("Length: " + i);
            if(i>15) {
                receivedData=receivedData.substring(0,firstInd+1);
            }
            else {
                receivedData="Not correct";
                System.out.println("Not correct");
            }
            /*
             * Преобразуйте отправленные клиентом данные в верхний регистр,
             * Преобразуйте их в байты
             * и сохраните в соответствующий буфер. */
             // System.out.println(receivedData.toUpperCase());
              sendingDataBuffer = receivedData.toUpperCase().getBytes();

            // Получите IP-адрес и порт клиента
            InetAddress senderAddress = inputPacket.getAddress();
            int senderPort = inputPacket.getPort();

            // Создайте новый UDP-пакет с данными, чтобы отправить их клиенту
            DatagramPacket outputPacket = new DatagramPacket(
                    sendingDataBuffer, sendingDataBuffer.length,
                    senderAddress,senderPort
            );

            // Отправьте пакет клиенту
             serverSocket.send(outputPacket);
           //serverSocket.send(inputPacket);

            // Закройте соединение сокетов
            serverSocket.close();
        }
        catch (SocketException e){
            e.printStackTrace();
        }
    }
}