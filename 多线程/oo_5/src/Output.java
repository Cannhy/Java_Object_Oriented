import com.oocourse.elevator1.TimableOutput;

public class Output {
    public static synchronized void print(String out) {
        TimableOutput.println(out);
    }
}
