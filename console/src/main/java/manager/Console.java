package manager;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;

public class Console {
    private static Stack<InputStream> inStreams = new Stack<>();
    private static Stack<PrintStream> outStreams = new Stack<>();
    private static InputStream in = System.in;
    private static PrintStream out = System.out;
    private static Scanner scanner = new Scanner(in);
    private static boolean isWorking = true;

    public static void print(String s){
        out.println(s);
    }

    public static String input(){
        return scanner.nextLine();
    }

    private static void print(String s, String color){
        out.print(color + s + "\u001B[0m");
    }

    public static void printError(String e){
        print(e + "\n", "\u001B[31m") ;
    }

    public static void printError(Exception e){
        printError(e.getMessage());
    }

    public static void swapInputStream(InputStream stream){
        inStreams.push(in);
        in = stream;
        scanner = new Scanner(in);
    }

    public static boolean canRead(){
        if (scanner.hasNext()){
            return isWorking;
        }

        if (inStreams.size() == 0){
            return false;
        }
        in = inStreams.pop();
        scanner = new Scanner(in);
        return isWorking && canRead();
    }

    public static void exit(){
        isWorking = false;
    }
}
