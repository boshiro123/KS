import java.io.*;
        import java.net.*;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.*;

        import static java.lang.System.exit;

// Server class
public class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(12345);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private DataBaseHandler dbHandler = new DataBaseHandler();

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            ObjectInputStream inputStream;

            try {

                // get the outputstream of client ДЛЯ ОТПРАВЛЕНИЯ КЛИЕНТУ
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // get the inputstream of client ДЛЯ ЧТЕНИЯ ОТ КЛИЕНТА
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

                String line;
                int choice;
                while ((line = in.readLine()) != null) {
                    try {
                        choice = Integer.parseInt(line);
                    }catch (NumberFormatException e){
                        System.out.println(e);
                        continue;
                    }
                    switch (choice){
                        case 1:
                            System.out.println(" 1 - пункт");
                            ResultSet resQuest = dbHandler.getQuestions();
                            List<Questions> questions = new ArrayList<>();
                            while(resQuest.next()){
                                questions.add(new Questions(resQuest.getInt(1),resQuest.getString(2),resQuest.getString(3),resQuest.getString(4),
                                        resQuest.getString(5),resQuest.getString(6)));
                            }
                            outputStream.writeObject(questions);
                            double sum=0, ball = 10/questions.size();
                            for(Questions i: questions){
                                String answer = in.readLine();
                                if(i.getTrueAnswer().equals(answer)){
                                    System.out.println("Верно!");
                                    sum+=ball;
                                }
                                else System.out.println("Неверно :(");
                            }

                            out.println("Балл: " + sum);
                            out.flush();
                            break;
                        case 2:
                            System.out.println("2 - пункт");
                            ResultSet resQuest1 = dbHandler.getQuestions();
                            List<Questions> questions1 = new ArrayList<>();
                            while(resQuest1.next()){
                                questions1.add(new Questions(resQuest1.getInt(1),resQuest1.getString(2),resQuest1.getString(3),
                                        resQuest1.getString(4), resQuest1.getString(5),resQuest1.getString(6)));
                            }
                            outputStream.writeObject(questions1);
                            int id = Integer.parseInt(in.readLine());
                            Questions redact = (Questions) inputStream.readObject();
                            System.out.println(id);
                            dbHandler.UpdateQuestion(redact,id);
                            System.out.println("Успешено изменено");
                            break;
                        default:
                            System.out.println("Ошибочка");
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (out != null) {
                        System.out.println("Отправление закрыто");
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        System.out.println("Принятие закрыто");
                        clientSocket.close();
                        Runtime.getRuntime().exit(0);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

}
}