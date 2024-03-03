import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private int id;
    private static int counter = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
    }

    public void print() {
        counter++;
        if (idCounter.containsKey(id)) {
            int oldCount = idCounter.get(id);
            idCounter.put(id, oldCount + 1);
        } else {
            idCounter.put(id, 1);
        }
        System.out.println("pinf-" + counter + ", " + id + "-" + idCounter.get(id));
    }
}
