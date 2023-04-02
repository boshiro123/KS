import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8001;
    public static void main(String[] args){
        Server server = new Server();
        server.start();
    }

    public void start(){
        try{
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");
            while (true) {
                Socket clientSocket = server.accept();
                ClientHolder client = new ClientHolder(clientSocket, this);

                new Thread(client).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToAll(String msg, Cl)
    {
        client.sendMsg();
    }
}
