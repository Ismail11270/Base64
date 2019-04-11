package pl.polsl.ismoil.atajanov.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  Class View
 *  implements input/output of the program
 *  @author Ismoil Atajanov
 *  @version 1.0
 */
public class View {
    /**
     *  Scanner for reading data from standard input stream
     */
    private final Scanner scanner = new Scanner(System.in);
    /**
     *  Scanner for reading data from text files
     */
    private Scanner fileScanner;

    /**
     * Print message to Standard output
     * @param message - String to print
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Read String input from standard input
     * @return String user input
     */
    public String readInput() {
        if(scanner.hasNextLine()){
            return scanner.nextLine();
        }
        return "";
    }

    /**
     * Read integer input from standard input
     * @return integer user input
     */
    public int readInt(){
        while(true){
            if(scanner.hasNextInt()){
                return scanner.nextInt();
            }else{
                System.out.println("Wrong input, try again.");
                clearInput();
            }
        }
    }

    /**
     * Method that read from file and saves into a String
     * @param file - File to read from
     * @return  String of text read from file
     * @throws FileNotFoundException , if file was not found
     */
    public String readFromFile(File file) throws FileNotFoundException{
        String text = "";
        try {
            fileScanner = new Scanner(file);
            while(fileScanner.hasNextLine()){
                text+=fileScanner.nextLine();
            }
        }catch(FileNotFoundException e){
            throw e;
        }

        return text;
    }

    /**
     * Clear the input stream in case of scanning failure
     */
    public void clearInput() { scanner.next(); }

}
