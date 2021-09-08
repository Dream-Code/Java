/**
 * Name: Steven Rivera
 * 
 * This Java program is a simple subtraction quiz that takes in user input to guess the difference between
 * two random numbers
 */
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SubtractionQuiz {
    public static void main(String[] args) throws InterruptedException {
    
        // 1. Generate two random single-digit integers
        int number1 = (int)(Math.random() * 10);
        int number2 = (int)(Math.random() * 10);

        // 2. If number1 < number2, swap number1 with number2
        // puts number1 into a temporary "box" so that we can put number2 into
        // the box, then changes number2 to number1 then puts number2 into
        // the temp box thereby creating a new variable to print to console
        if (number1 < number2) {
            int temp = number1;
            number1 = number2;
            number2 = temp;
        }

        // 3. Prompt the student to answer â€�What is number1 â€“ number2?â€�
        System.out.print("What is " + number1 + " - " + number2 + "? ");
        Scanner input = new Scanner(System.in);
        int answer = input.nextInt();
        
        //1 second delay
        TimeUnit.SECONDS.sleep(1);
        System.out.println("You entered: " + answer);
        
        //1 second delay
        TimeUnit.SECONDS.sleep(1);

        // 4. Grade the answer and display the result
        if (number1 - number2 == answer)
        System.out.println("You are correct!");
        else {
        //1 second delay
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Your answer is wrong.");
        System.out.println(number1 + " - " + number2 +" should be " + 
            (number1 - number2));
        }
    }
}