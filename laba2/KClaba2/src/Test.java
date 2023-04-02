import java.util.Scanner;

public class Test {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Input string: ");
            String string = input.nextLine();
            int firstInd=0,secondInd,test=0;
            if(string.equals("kill")){
                System.out.println("The End");
                break;
            }
            System.out.println(string.length());
            char[] str = string.toCharArray();
            for(int i=0;i<string.length();i++) {
                if (Character.isDigit(str[i])) {
                    secondInd=i;
                    while(Character.isDigit(str[secondInd]) && secondInd<string.length()){
                       test=test*10+Integer.parseInt(String.valueOf(str[secondInd]));
                        secondInd++;
                    }
                    if(test%3==0){
                        for(firstInd=i; firstInd<=secondInd;firstInd++){
                            str[firstInd]=' ';
                        }
                    }
                }
            }
            System.out.println((String.valueOf(str)).replace(" ",""));
        }
    }
}
