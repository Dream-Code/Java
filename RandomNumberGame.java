/**
 * Name: Steven Rivera
 * 
 * This program ask the user to pick a number between 1-10 and check it against a random number. It
 * will also use an if/else statement to determine if the number is greater than 10 or less 
 * than 1 and, if true, print an error message to console
 * 
 */

import java.util.Scanner;

public class RandomNumberGame {

    public static void main(String[] args) {
        
        //Generate a random number between 1-10
        int randNum = (int)(Math.random() * 10);
        
        double realNum;         //Stores the user guess
        boolean win = false;    //variable to check if the user won
        int numTries = 1;       //Holds the number of attempts made
        
        //While loop to keep game running until the win condition is met
        while (win == false){
            
            //Scanner class to get the user's guess
            Scanner userNum = new Scanner(System.in);
            System.out.print("Please enter a number between 1-10: ");
            realNum = (int)userNum.nextInt(); //stores the user guess in realNum
            
           
            //Standard if/else-if/else statement to determine if the user 
            //guesses correctly.
            if (realNum == randNum) {
                win = true;  //if the win condition is met, changes win to true
                System.out.println("    ***************");
                System.out.println("    ** You Win!! **");
                System.out.println("    ***************");
                System.out.println("The number was " + randNum + " and you chose " 
                      + (int)realNum + " in " + numTries + " attempts.");
            } 
            else if (realNum != randNum && realNum >= 1 && realNum <= 10) {
                System.out.println("Incorrect. Please try again \n");
                numTries++;  //if the guess is wrong increments numTries by 1
            } 
            else {
                System.out.println("ERROR. Please choose a number "
                        + "between 1-10. \n");
                numTries++;
            }
        
        }

    }
    
}
