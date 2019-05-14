package pl.polsl.ismoil.atajanov.control;

import pl.polsl.ismoil.atajanov.model.Base64;
import pl.polsl.ismoil.atajanov.model.base64exceptions.EncodedStringInvalidException;
import pl.polsl.ismoil.atajanov.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Vector;
/**
 * Class controlling view and model
 * of the program
 *
 * @author Ismoil Atajanov
 * @version 1.0
 */
public class Controller {
    /**
     * View object controlling IO stream of the program
     */
    private View view;
    /**
     * Base64 object performing encoding and decoding
     */
    private Base64 base64;
    /**
     * File to be used for encoding/decoding
     */
    private File file;

    /**
     * Create controller where file is defined as a main arg
     *
     * @param view   initialized object of View class
     * @param base64 initialized object of Base64 class
     * @param file   File initializer
     */
    public Controller(View view, Base64 base64, File file) {
        this.view = view;
        this.base64 = base64;
        this.file = file;
    }

    /**
     * Create Controller with no file defined
     *
     * @param view   View object
     * @param base64 Base64 object
     */
    public Controller(View view, Base64 base64) {
        this(view, base64, new File(""));
    }

    /**
     * Forces user to choose the program to execute
     * base64Encode or base64Decode
     *
     * @return int decision
     */
    public int encodeOrDecode() {
        view.print("Enter 1 for encoding or 2 for decoding, or any other number to exit: ");
        return view.readInt();
    }

    /**
     * Executes encoder function,
     * Trying to read from file, if failed asks user for input
     */
    public void base64Encode() {
        try {
            view.print(base64.encode(view.readFromFile(file)));
        } catch (FileNotFoundException e) {
            view.print("No file found, please enter the text to encode manually:\n");
            view.readInput();
            view.print(base64.encode(view.readInput()));
        }
    }

    /**
     * Executes decoder function,
     * Trying to read from file, in case of failure calls base64DecodeInput method that takes user input to decode
     */
    public void base64Decode() {
        String inputFromFile = "";
        try {
            inputFromFile=view.readFromFile(file);
            view.print(base64.decode(inputFromFile));
        } catch (EncodedStringInvalidException e) {  //If file contains " "
            view.print("The input string contains spaces, they are ignored during decoding.\n");
            try{
                view.print(base64.decode(removeSpacesFromString(inputFromFile)));
            }catch(EncodedStringInvalidException e2){
                view.print("Something went wrong while removing spaces from the string!\n");
                return;
            }
        } catch (FileNotFoundException e) {  //if no file given
            view.print("No file found, please enter the text to encode manually.\n");
            view.readInput();
            base64DecodeInput();
        }
    }

    /**
     * Method that runs if the file is not found
     * and takes user input to decode
     */
    public void base64DecodeInput() {
        String input = "";
        try {
            view.print("Enter a string to decode pls: \n");
            input = view.readInput();
            view.print(base64.decode(input));
        } catch (EncodedStringInvalidException e1) {
            view.print(e1.getMessage() + "\nPress any key to ignore all the spaces in the string or \"Q\" to quit: ");
            String choice = view.readInput();
            if(choice.toLowerCase() == "q") {
                return;
            }else {
                try {
                    view.print(base64.decode(removeSpacesFromString(input)));
                } catch (EncodedStringInvalidException e) {
                    view.print("Error occured\n");
                }
            }
        }
    }

    /**
     * The method removes spaces from a string
     * @param text - String
     * @return String without spaces
     */
    public String removeSpacesFromString(String text) {
        Character[] textAsCharacterArray =
                text.chars().mapToObj(c -> (char) c).toArray(Character[]::new);

        Vector<Character> textToCharList = new Vector<>(Arrays.asList(textAsCharacterArray));

        while (textToCharList.contains(' ')) {
            textToCharList.remove(textToCharList.indexOf(' '));
        }

        String updatedText = "";
        for (char a :
                textToCharList) {
            updatedText = updatedText.concat(Character.toString(a));
        }
        view.print(updatedText + "\n");
        return updatedText;
    }

}
