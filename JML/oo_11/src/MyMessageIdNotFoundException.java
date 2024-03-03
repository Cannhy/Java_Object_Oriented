import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static int counter = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
    }

    @Override
    public void print() {
        counter++;
        if (idCounter.containsKey(this.id)) {
            int oldCnt = idCounter.get(this.id);
            idCounter.put(id, oldCnt + 1);
        } else {
            idCounter.put(id, 1);
        }
        System.out.println("minf-" + counter + ", " + id + "-" + idCounter.get(id));
    }
}