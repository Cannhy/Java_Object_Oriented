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
        Department department = new Department();
        int t = Integer.parseInt(scanner.nextLine().trim());
        department.addBooks(scanner, t);
        int m = Integer.parseInt(scanner.nextLine().trim());
        department.run(scanner, m);
    }
}