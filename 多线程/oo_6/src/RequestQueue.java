import java.util.ArrayList;
import java.util.HashMap;

public class RequestQueue {
    private Boolean endTag = false;
    private final HashMap<Integer, ArrayList<Person>> requestMap = new HashMap<>();

    public synchronized void addPerson(Integer integer, Person person) {
        if (!requestMap.containsKey(integer)) {
            ArrayList<Person> temp = new ArrayList<>();
            temp.add(person);
            requestMap.put(integer, temp);
        } else {
            requestMap.get(integer).add(person);
        }
    } // 生产者方法

    public synchronized void delPerson(Person person) {
        this.requestMap.get(person.getSrc()).remove(person);
    } //消费者方法

    public synchronized boolean isNull() {
        for (ArrayList<Person> temp : this.getRequestMap().values()) {
            for (Person person : temp) {
                if (!person.getMain()) {
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized boolean isNull2() {
        for (ArrayList<Person> temp : this.getRequestMap().values()) {
            if (!temp.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void setOver(Boolean flag) {
        this.endTag = flag;
    }

    public synchronized Boolean isOver() {
        return endTag.equals(true);
    }

    public HashMap<Integer, ArrayList<Person>> getRequestMap() {
        return requestMap;
    }

}
