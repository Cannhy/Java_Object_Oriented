import com.oocourse.elevator2.TimableOutput;

public class Output {
    public static synchronized void print(String out) {
        TimableOutput.println(out);
    }
}
