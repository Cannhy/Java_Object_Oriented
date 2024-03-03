import com.oocourse.spec3.main.Runner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        File filer = new File("in.txt");
        FileInputStream fis = new FileInputStream(filer);
        System.setIn(fis);
        PrintStream ps = new PrintStream(new FileOutputStream("out.txt"));
        System.setOut(ps);
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class,
                MyEmojiMessage.class, MyNoticeMessage.class, MyRedEnvelopeMessage.class);
        runner.run();
    }
}