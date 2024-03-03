import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private int id;
    private static int counter = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();

    public MyEqualEmojiIdException(int id) {
        this.id = id;
    }

    @Override
    public void print() {
        counter++;
        if (idCounter.containsKey(this.id)) {
            int old = idCounter.get(this.id);
            idCounter.put(this.id, old + 1);
        } else {
            idCounter.put(this.id, 1);
        }
        System.out.println("eei-" + counter + ", " + id + "-" + idCounter.get(id));
    }
}