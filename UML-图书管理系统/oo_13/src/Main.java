import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        File filer = new File("in.txt");
        FileInputStream fis = new FileInputStream(filer);
        System.setIn(fis);
        PrintStream ps = new PrintStream(Files.newOutputStream(Paths.get("out.txt")));
        System.setOut(ps);
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        int n = Integer.parseInt(scanner.nextLine().trim());
        library.addBooks(scanner, n);
        int m = Integer.parseInt(scanner.nextLine().trim());
        library.run(scanner, m);
    }
}