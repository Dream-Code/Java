/**
 * Name: Steven Rivera
 * 
 * This program is the classic fizz/buzz problem. It will count to 100. If the
 * number is divisible by 3 it will replace the number with "fizz", if it is
 * divisible by 5 it will replace the number with "buzz". If the number is
 * divisible by both 3 and 5 it will replace the word with "FizzBuzz"
 * 
 * **To note: I also print the number being replaced just for verification**

 */
public class FizzBuzz {

    public static void main(String[] args) {
        
        int num;
        String fizz = "Fizz";
        String buzz = "Buzz";
        String fizzBuzz = "FizzBuzz";
        
        for(num = 0; num <= 100; num++){
            if(num % 3 == 0 && num % 5 == 0){
               System.out.println(fizzBuzz + " " + num);
            }
            else if(num % 3 == 0){
                System.out.println(fizz + " " + num);
            }
            else if(num % 5 == 0){
                System.out.println(buzz + " " + num);
            }
            else{
                System.out.println(num);
            }
        }
    }
}
