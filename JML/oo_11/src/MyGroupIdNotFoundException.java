import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id;
    private static int counter = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();

    public MyGroupIdNotFoundException(int id) {
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
        System.out.println("ginf-" + counter + ", " + id + "-" + idCounter.get(id));
    }
}
