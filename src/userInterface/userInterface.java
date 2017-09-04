package userInterface;

import exceptions.InvalidSequenceException;
import vitterImpl.VTree;
import exceptions.InvalidCommandException;

import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This maintains and manages the user facing part of the program, additionally performing experiments as requested.
 */
public class userInterface {

    public static void main(String[] args){
        decipher(args);
    }

    public static void decipher(String[] args){
        StringTokenizer st = new StringTokenizer(convertArrayToString(args));
        boolean encode;
        String input, output;
        int symbols;
        try{
            encode = decipherPurpose(st.nextToken());
            input = decipherInput(st.nextToken());
            output = decipherOutput(st.nextToken());
            symbols = decipherSymbols(st.nextToken());
            VTree t = createVTree(output, symbols);
            if (encode){
                encode(t, input);
            }else{
                decode(t, input);
            }
        }catch (InvalidCommandException e){
            System.out.println("Invalid command!");
            System.out.println("Please maintain format: <e(ncode) or d(ecode)> <file input> <file output> " +
                    "<number of symbols>");
            System.out.println("Example: e input.txt output.txt");
        }catch(IOException e){
            System.out.println("Error reading or writing to file. Ensure file is not being accessed and you have " +
                    "permission to write or read it (as appropriate)");
        }catch(InvalidSequenceException e){
            System.out.println("That code was not valid for these parameters. ");
        }catch (NumberFormatException e){
            System.out.println("The number of symbols you requested was not valid.");
        }
    }

    private static VTree createVTree(String output, int symbols) throws IOException{
        return new VTree(symbols, output);
    }

    private static void encode(VTree t, String input) throws IOException{
        t.encode(new FileReader(input));
    }

    private static void decode(VTree t,String input) throws IOException, InvalidSequenceException{
        t.decode(new FileReader(input));
    }

    private static boolean decipherPurpose(String s) throws InvalidCommandException{
        switch (s) {
            case "e":
            case "-e":
            case "encode":
            case "-encode": return true;
            case "d":
            case "-d":
            case "decode":
            case "-decode": return false;
            default: throw new InvalidCommandException();
        }
    }

    private static int decipherSymbols(String s) throws NumberFormatException{
        if (s == null) return 1;
        return Integer.decode(s);
    }

    private static String decipherInput(String s) throws InvalidCommandException{
        if (s == null) throw new InvalidCommandException();
        return s;
    }

    private static String decipherOutput(String s){
        if (s == null) return "output.txt";
        return s;
    }

    private static String convertArrayToString(String[] args){
        StringBuilder sb = new StringBuilder();
        for (String s: args){
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }
}
