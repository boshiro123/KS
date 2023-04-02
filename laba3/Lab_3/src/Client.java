import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.System.*;

// Client class
public class Client {

    // driver code
    public static void main(String[] args)
    {
        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 12345);
             Scanner input = new Scanner(socket.getInputStream());) {
            Scanner inputStr = new Scanner(System.in);
            Scanner inputInt = new Scanner(System.in);

            // writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;

            while (!"kill".equalsIgnoreCase(line)) {

                // reading from user
                System.out.print("1 — просмотр команд\n" +
                        "2 — добавление команды\n" +
                        "3 — удаление команды\n" +
                        "4 — редактирование команды\n"+
                        "5 — поиск команды по возрасту\n" +
                        "Input: ");
                line = sc.nextLine();

                // sending the user input to server
                out.println(line);
                out.flush();
                // displaying server reply
                try {
                    switch (Integer.parseInt(line)) {
                        case 1:
                            System.out.println("Пункт - 1");
                            new Thread(() -> {
                                try {
                                    while (input.hasNext()) {
                                        String message = input.nextLine();
                                        System.out.println(message);
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                }
                            }).start();
                            Thread.sleep(1000);
                            System.out.println("The end");
                            break;
                        case 2:
                            System.out.println("Пункт - 2");
                            DanceTeam newTeam = new DanceTeam();
                            System.out.print("Имя команды: ");
                            newTeam.setTeamName(inputStr.nextLine());
                            System.out.print("Город:");
                            newTeam.setCity(inputStr.nextLine());
                            System.out.print("Оценка жюри: ");
                            newTeam.setJuryScore(inputInteger());
                            System.out.print("Оценка зала: ");
                            newTeam.setAuidScore(inputInteger());
                            System.out.print("Возраст команды: ");
                            int age = inputInt.nextInt();
                            if (age > 0 && age <= 14) newTeam.setAgeGroup("Дети");
                            else if (age >= 15 && age <= 64) newTeam.setAgeGroup("Взрослые");
                            else if (age >= 65) newTeam.setAgeGroup("Пожилые");
                            else if(age<0)newTeam.setAgeGroup("Кто ты?");
                            outputStream.writeObject(newTeam);
                            break;
                        case 3:
                            System.out.println("Пункт - 3");
                            new Thread(() -> {
                                try {
                                    while (input.hasNext()) {
                                        String message = input.nextLine();
                                        System.out.println(message);
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                }
                            }).start();
                            Thread.sleep(1000);
                            System.out.print("Введите имя команды для удаления \nInput: ");
                            out.println(inputStr.nextLine());
                            out.flush();
                            Thread.sleep(1000);
                            break;
                        case 4:
                            System.out.println("Пункт - 4");
                            System.out.println("Выберите имя команды для редактирования \nInput:");
                            out.println(inputStr.nextLine());
                            out.flush();
                            Thread.sleep(1000);
                            String check = in.readLine();
                            System.out.print("");
                            if(check.equals("Команда есть")){DanceTeam updateTeam = new DanceTeam();
                                System.out.print("Имя команды: ");
                                updateTeam.setTeamName(inputStr.nextLine());
                                System.out.print("Город:");
                                updateTeam.setCity(inputStr.nextLine());
                                System.out.print("Оценка жюри: ");
                                updateTeam.setJuryScore(inputInteger());
                                System.out.print("Оценка зала: ");
                                updateTeam.setAuidScore(inputInteger());
                                System.out.print("Возраст команды: ");
                                int newAge = inputInt.nextInt();
                                if (newAge > 0 && newAge <= 14) updateTeam.setAgeGroup("Дети");
                                else if (newAge >= 15 && newAge <= 64) updateTeam.setAgeGroup("Взрослые");
                                else if (newAge >= 65) updateTeam.setAgeGroup("Пожилые");
                                else if(newAge<0)updateTeam.setAgeGroup("Кто ты?");
                                outputStream.writeObject(updateTeam);
                            }
                            else if(check.equals("Такой команды нет")) System.out.println("Такой команды нет");
                            Thread.sleep(1000);
                            break;
                        case 5:
                            String ageGrop="";
                            int ageForSort;
                            System.out.print("Введите возраст группы \nInput: ");
                            ageForSort=inputInteger1();
                            if (ageForSort > 0 && ageForSort <= 14) ageGrop="Дети";
                            else if (ageForSort >= 15 && ageForSort <= 64) ageGrop="Взрослые";
                            else if (ageForSort >= 65) ageGrop = "Пожилые";
                            else if(ageForSort<0)ageGrop="Кто ты?";
                            out.println(ageGrop);
                            out.flush();
                            String msg = in.readLine();
                            System.out.println(msg);
                            break;
                        default:
                            System.out.println("Ошибочка");
                    }
                    line=null;//ПРОВЕРИТЬ!!!
                }catch (NumberFormatException e){
                    if(line.equals("kill")) {
                        System.out.println("Client initialize suicide");
                        exit(0);
                    }
                    else System.out.println(e);
                }

            }

            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static int inputInteger(){
        Scanner input = new Scanner(System.in);
        String str;
        int number;
        while (true){
            try {
                str = input.nextLine();
                number = Integer.parseInt(str);

            }catch (NumberFormatException e){
                System.out.println("Некорректный ввод, попробуйте снова");
                continue;
            }
            if (number >= 0 && number <= 10) {
                return number;
            } else {
                System.out.println("Некорректный ввод, попробуйте снова1");
                continue;
            }
        }
    }
    public static int inputInteger1(){
        Scanner input = new Scanner(System.in);
        String str;
        int number;
        while (true){
            try {
                str = input.nextLine();
                number = Integer.parseInt(str);
                return number;
            }catch (NumberFormatException e){
                System.out.println("Некорректный ввод, попробуйте снова");
                continue;
            }
        }
    }

}
