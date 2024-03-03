import com.oocourse.spec2.main.Runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("in.txt");
        FileInputStream fis = new FileInputStream(file);
        System.setIn(fis);
        PrintStream ps=new PrintStream(new FileOutputStream("out.txt"));
        //将标准输出重定向到PS输出流
        System.setOut(ps);
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class,
                MyMessage.class);
        runner.run();
    }
}