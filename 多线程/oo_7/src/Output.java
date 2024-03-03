import com.oocourse.elevator3.TimableOutput;

public class Output {
    public static synchronized void print(String out) {
        TimableOutput.println(out);
    }
}
