import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

// Client class
public class Client2 {

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
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;

            while (!"kill".equalsIgnoreCase(line)) {

                // reading from user
                System.out.print("1 — ответить на вопросы\n" +
                        "2 — редактировать вопросы\n" +
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
                            List<Questions> questions = (List<Questions>)inputStream.readObject();
                            for(Questions i:questions){
                                System.out.print(i.toString()+"\nInput: ");
                                String answer = inputStr.nextLine();
                                out.println(answer);
                                out.flush();
                            }
                            Thread.sleep(1000);
                            System.out.println(in.readLine());
                            break;
                        case 2:
                            System.out.println("Пункт - 2");
                            List<Questions> questions1 = (List<Questions>)inputStream.readObject();
                            Questions redact = new Questions();
                            for(Questions i:questions1){
                                System.out.println(i.getQuestion() +", id = " +i.getId());
                            }
                            System.out.println("Выберите id вопроса для редактирования \nInput: ");
                            String id = inputStr.nextLine();
                            out.println(id);
                            out.flush();
                            System.out.println("Вопрос: ");
                            redact.setQuestion(inputStr.nextLine());
                            System.out.println("Первый ответ: ");
                            redact.setFirstAnswer(inputStr.nextLine());
                            System.out.println("Второй ответ: ");
                            redact.setSecondAnswer(inputStr.nextLine());
                            System.out.println("Третий ответ: ");
                            redact.setThirdAnswer(inputStr.nextLine());
                            System.out.println("Правильный ответ: ");
                            redact.setTrueAnswer(inputStr.nextLine());
                            outputStream.writeObject(redact);
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
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
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
