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
    /*public void sendWays(ClientHandler client) throws SQLException, ClassNotFoundException {
        boolean flag = false;
        DataBaseHandler dbHandler = new DataBaseHandler();
        ResultSet teams = dbHandler.getTeams();
        dt = new ArrayList<>();
        while (teams.next()) {
            dt.add(new DanceTeam(teams.getString(2), teams.getString(3),
                    teams.getInt(4), teams.getInt(5), teams.getString(6)));
        }
        for(DanceTeam way : dt)
        {
            client.sendMsg(way.toString());
            flag = true;
        }
        if(!flag)
        {
            System.out.println("Nothing to sent");
            client.sendMsg("There is no one stop with this name in list");
        }
        else
        {
            System.out.println("Stop list sent");
        }
    }

     */

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
                            soutTeams();
                            break;
                        case 2:
                            System.out.println("2 - пункт");
                            DanceTeam newDT = (DanceTeam) inputStream.readObject();
                            dbHandler.registerTeam(newDT);
                            System.out.println("Успешено добавлено");
                            break;
                        case 3:
                            System.out.println("3 - пункт");
                            soutTeams();
                            String teamName = in.readLine();
                            if(checkTeam(teamName)){
                                dbHandler.DeleteTeam(teamName);
                                Thread.sleep(500);
                                out.println("Команда удалена");
                                out.flush();
                            }
                            else{
                                out.println("Такой команды нет");
                                out.flush();
                            }
                            break;
                        case 4:
                            System.out.println("4 - пункт");
                            String updateTeamName = in.readLine();
                            System.out.println("Получено: "+ updateTeamName);
                            if(checkTeam(updateTeamName)){
                                System.out.println("Команда есть");
                                out.println("Команда есть");
                                out.flush();
                                DanceTeam updateDT = (DanceTeam) inputStream.readObject();
                                dbHandler.UpdateTeam(updateDT,updateTeamName);
                                Thread.sleep(500);
                                out.println("Команда редактирована");
                                out.flush();
                            }
                            else{
                                out.println("Такой команды нет");
                                out.flush();
                            }
                            break;
                        case 5:
                            String age = in.readLine();
                            System.out.println("Получено: "+ age);
                            ResultSet teams = dbHandler.getTeamsForAge(age);
                            ArrayList<DanceTeam> dt = new ArrayList<>();
                            while (teams.next()) {
                                dt.add(new DanceTeam(teams.getString(2), teams.getString(3),
                                        teams.getInt(4), teams.getInt(5), teams.getString(6)));
                            }
                            dt.sort(Comparator.comparing(DanceTeam::getJuryScore).thenComparing(DanceTeam::getAuidScore));
                            Collections.reverse(dt);
                            out.println(dt.get(0).toString());
                            out.flush();
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
            } catch (InterruptedException e) {
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
        public void soutTeams() throws SQLException, ClassNotFoundException, IOException {
            DataBaseHandler dbHandler = new DataBaseHandler();
            PrintWriter  out = new PrintWriter(clientSocket.getOutputStream(), true);
            ResultSet teams = dbHandler.getTeams();
            List<DanceTeam> dt = new ArrayList<>();
            while (teams.next()) {
                dt.add(new DanceTeam(teams.getString(2), teams.getString(3),
                        teams.getInt(4), teams.getInt(5), teams.getString(6)));
            }
            for(DanceTeam i: dt){
                out.println(i.toString());
                out.flush();
            }
        }
        public boolean checkTeam(String name) throws SQLException, ClassNotFoundException, IOException {
            DataBaseHandler dbHandler = new DataBaseHandler();
            PrintWriter  out = new PrintWriter(clientSocket.getOutputStream(), true);
            ResultSet teams = dbHandler.getTeams();
            List<DanceTeam> dt = new ArrayList<>();
            while (teams.next()) {
                dt.add(new DanceTeam(teams.getString(2), teams.getString(3),
                        teams.getInt(4), teams.getInt(5), teams.getString(6)));
            }
            for(DanceTeam i: dt){
                if(name.equals(i.getTeamName()))return true;
            }
            return false;
        }

    }

}