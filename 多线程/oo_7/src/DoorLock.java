import java.util.ArrayList;

public class DoorLock {
    private static ArrayList<Object> locks = new ArrayList<>();
    private static int [] serving = new int[12];
    private static int [] onlyRev = new int[12];

    public static ArrayList<Object> getLocks() {
        return locks;
    }

    public static int[] getServing() {
        return serving;
    }

    public static int[] getOnlyRev() {
        return onlyRev;
    }
}
