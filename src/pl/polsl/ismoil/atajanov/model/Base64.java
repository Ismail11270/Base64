package pl.polsl.ismoil.atajanov.model;

import pl.polsl.ismoil.atajanov.model.base64exceptions.EncodedStringInvalidException;

import java.util.*;

/**
 * Class implements encoding and decoding of text using Base64
 * @author Ismoil Atajanov
 * @version 1.0
 */
public class Base64{
    /**
     *  Map of containing base64 table
     *  In the current version used only for verifying encoded string
     */
    private Map<Integer,Character> base64Table;

    /**
     * Constructor of the class, only initializes the base64 table map
     */
    public Base64(){
        initializeBase64Table();
    }
    /**
     * Method to encode text using base64 method
     * @param textToEncode text to be encoded
     * @return encoded String
     */
    public String encode(String textToEncode) {
        String input = textToEncode;
        String output = "";
        int[] asciiInput = new int[3];
        char[] asciiOutput = new char[4];
        String concatAllBits = "";
        int n = 0;
        //System.out.println(input.length());
        while (n < input.length() - 2) {
            String cutThree = input.substring(n, n + 3);
            asciiInput[0] = cutThree.toCharArray()[0];
            asciiInput[1] = cutThree.toCharArray()[1];
            asciiInput[2] = cutThree.toCharArray()[2];

            concatAllBits = intToEightBitsString(asciiInput[0]).concat(intToEightBitsString(asciiInput[1])).concat(intToEightBitsString(asciiInput[2]));
            output = output.concat(convertThreeBitsToFour(concatAllBits));

            n += 3;
        }
        //1 missing
        if (n == input.length() - 2) {
            String cutTwo = input.substring(n, n + 2);
            asciiInput[0] = cutTwo.toCharArray()[0];
            asciiInput[1] = cutTwo.toCharArray()[1];
            concatAllBits = intToEightBitsString(asciiInput[0]).concat(intToEightBitsString(asciiInput[1])).concat("00000000");
            output = output.concat(convertThreeBitsToFour(concatAllBits));
            output = output.substring(0, output.length() - 1).concat("=");
        }
        //2 missing
        else if (n == input.length() - 1) {
            String cutOne = input.substring(n, n + 1);
            asciiInput[0] = cutOne.toCharArray()[0];
            concatAllBits = intToEightBitsString(asciiInput[0]).concat("00000000").concat("00000000");
            output = output.concat(convertThreeBitsToFour(concatAllBits));
            output = output.substring(0, output.length() - 2).concat("==");
        }
        return output;
    }

    /**
     * Method decode String using Base64 method
     * @param stringToDecode string to be decoded
     * @throws EncodedStringInvalidException if stringToDecode contains spaces
     * @return String - result of decoding
     */
    public String decode(String stringToDecode) throws EncodedStringInvalidException {
        String input = stringToDecode;
        if(!isDecodable(input)) {
            throw new EncodedStringInvalidException("The input to decode contains spaces");
        }
        String output = "";
        int[] base64Indexes = new int[4];
        int n = 0;
        String concatAllBits = "";
        while (n < input.length() - 3) {

            String cutFour = input.substring(n, n + 4);
            try {
                base64Indexes[0] = base64CharToIndex(cutFour.toCharArray()[0]);
                base64Indexes[1] = base64CharToIndex(cutFour.toCharArray()[1]);
                base64Indexes[2] = base64CharToIndex(cutFour.toCharArray()[2]);
                base64Indexes[3] = base64CharToIndex(cutFour.toCharArray()[3]);
            }catch(EncodedStringInvalidException e){
                throw e;
            }
            concatAllBits = intToSixBitsString(base64Indexes[0])
                    .concat(intToSixBitsString(base64Indexes[1]))
                    .concat(intToSixBitsString(base64Indexes[2]))
                    .concat(intToSixBitsString(base64Indexes[3]));
            output = output.concat(convertFourBitsToThree(concatAllBits));

            n += 4;
        }
        if(input.toCharArray()[input.length() - 1] == '='){
            output = output.substring(0,output.length() - 1);
        }

        return output;
    }

    /**
     * Method to cut 24 bits String into 4 and convert them using base 64 table into chars
     * using base64IndexToChar() method
     * Used to encode text
     * @param concatAllBits String of 24 bits obtained from 3 ASCII chars
     * @return String of 4 concatenated base64 chars as a result of conversion of 3 chars
     */
    private String convertThreeBitsToFour(String concatAllBits) {
        char[] bitsToBase64Char = new char[4];
        bitsToBase64Char[0] = base64IndexToChar(bitsToInt(concatAllBits.substring(0, 6)));
        bitsToBase64Char[1] = base64IndexToChar(bitsToInt(concatAllBits.substring(6, 12)));
        bitsToBase64Char[2] = base64IndexToChar(bitsToInt(concatAllBits.substring(12, 18)));
        bitsToBase64Char[3] = base64IndexToChar(bitsToInt(concatAllBits.substring(18, 24)));
        return "" + bitsToBase64Char[0] + bitsToBase64Char[1] + bitsToBase64Char[2] + bitsToBase64Char[3];
    }

    /**
     * Method to cut 24 bits string into 3 substrings and converted into chars
     * using ASCII table
     * Used to decode text
     * @param concatAllBits String of 24 bits obtained from 4 base64 chars
     * @return String of 3 concatenated ASCII chars
     */
    private String convertFourBitsToThree(String concatAllBits) {
        String output = "";
        char[] bitsToAscii = new char[3];
        bitsToAscii[0] = (char) bitsToInt(concatAllBits.substring(0, 8));
        bitsToAscii[1] = (char) bitsToInt(concatAllBits.substring(8, 16));
        bitsToAscii[2] = (char) bitsToInt(concatAllBits.substring(16, 24));
        return "" + bitsToAscii[0] + bitsToAscii[1] + bitsToAscii[2];
    }

    /**
     * Base64 table
     * @param x index from table
     * @return char from table
     */
    private char base64IndexToChar(int x) {
        if (x >= 0 && x <= 25) return (char) (65 + x);
        else if (x >= 26 && x <= 51) return (char) (97 + x - 26);
        else if (x >= 52 && x <= 61) return (char) (48 + x - 52);
        else if (x == 62) return '+';
        else if (x == 63) return '/';
        return 1;
    }

    /**
     * Base64 table
     * @param x char from table
     * @throws EncodedStringInvalidException if the string contains illegal characters
     * @return index from table
     */
    private int base64CharToIndex(char x) throws EncodedStringInvalidException {
        if (x >= 65 && x <= 90) return x - 65;
        else if (x >= 97 && x <= 122) return 26 + x - 97;         //26-51
        else if (x >= 48 && x <= 57) return 52 + x - 48;
        else if (x == '+') return 62;
        else if (x == '/') return 63;
        throw new EncodedStringInvalidException("The string contains illegal characters!! Cannot be decoded!");
    }

    /**
     * Convert integer into 8 bits binary
     * Used for encoding
     * @param x value from 0 - 128
     * @return String of 8 bits
     */
    private String intToEightBitsString(int x) {
        String result = "0";

        int remains = x;
        for (int j = 6; j >= 0; j--) {
            if (remains >= Math.pow(2, j)) {
                remains -= Math.pow(2, j);
                result = result.concat("1");
            } else result = result.concat("0");

        }
        return result;
    }

    /**
     * Convert integer into 6 bits binary
     * Used for decoding
     * @param x value from 0-64
     * @return String of 6 bits ( E.g. "001011")
     */
    private String intToSixBitsString(int x) {
        String res = "";

        int remains = x;
        for (int i = 5; i >= 0; i--) {
            if (remains >= Math.pow(2, i)) {
                remains -= Math.pow(2, i);
                res = res.concat("1");
            } else res = res.concat("0");
        }
        // System.out.println(res);
        return res;
    }

    /**
     * Convert binary String into integer
     * @param x binary String
     * @return decimal integer
     */
    private int bitsToInt(String x) {
        int result = 0;
        char[] xArray = x.toCharArray();        //000110

        for (int i = x.length() - 1; i >= 0; i--) {
            if (xArray[i] == '1') result += Math.pow(2, x.length() - 1 - i);
        }
        return result;
    }

    /**
     * Initializer method for the TreeMap field according to base64 table
     */
    private void initializeBase64Table() {
        base64Table = new TreeMap<>();
        for(int i = 0; i <= 25; i++){
            base64Table.put(i,(char)(65+i));
            base64Table.put(i+26,(char)(97+i));
        }
        for(int i = 0; i < 10; i++){
            base64Table.put(52+i,(char)(48+i));
        }
        base64Table.put(62,'+');
        base64Table.put(63,'/');
    }
    /**
     * Method that checks if the String contains any illegal characters
     * @param text encoded string
     * @return true or false if the string can be decoded
     */
    public boolean isDecodable(String text) {       //return false if contains spaces
        return (text.contains(" ")) ? false : true;
    }
}


