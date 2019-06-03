package pl.polsl.ismoil.atajanov.main;

import pl.polsl.ismoil.atajanov.control.Controller;
import pl.polsl.ismoil.atajanov.model.Base64;
import pl.polsl.ismoil.atajanov.view.View;

import java.io.File;

/**
 * Main class of the program
 * @author Ismoil Atajanov
 * @version 1.0
 */


public class Main {

    /**
     * main method of the program
     * calling all methods
     * @param args, args[0] is used as file path, can work with 0 args.
     */
    public static void main(String[] args) {
        Base64 base64 = new Base64();
        View view = new View();
        Controller controller;
        File file;
        if (args.length > 0) {
            file = new File(args[0]);
            controller = new Controller(view, base64, file);
        } else {
            controller = new Controller(view, base64);
        }

        switch (controller.encodeOrDecode()) {
            case 1: {
                controller.base64Encode();
                break;
            }
            case 2: {
                controller.base64Decode();
                break;
            }
            default:
                System.exit(0);
        }
    }//c3VrYSBibHlhZCBwaXpkZWMgbmFodWk=
}
