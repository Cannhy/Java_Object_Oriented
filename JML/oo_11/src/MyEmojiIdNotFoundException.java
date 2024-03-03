import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private int id;
    private static int counter = 0;
    private static HashMap<Integer, Integer> idCounter = new HashMap<>();

    public MyEmojiIdNotFoundException(int id) {
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
        System.out.println("einf-" + counter + ", " + id + "-" + idCounter.get(id));
    }
}