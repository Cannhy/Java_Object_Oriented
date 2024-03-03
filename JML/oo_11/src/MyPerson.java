import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private int socialValue;
    private int bestAcq;
    private int money;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;
    private List<Message> messages;
    private HashMap<Integer, Group> groups;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = 0;
        this.socialValue = 0;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.messages = new ArrayList<>();
        this.groups = new HashMap<>();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Person && ((Person) object).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return acquaintance.containsKey(person.getId()) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        return acquaintance.containsKey(person.getId()) ? value.get(person.getId()) : 0;
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    @Override
    public List<Message> getReceivedMessages() {
        List<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i <= 4 && i < messages.size(); i++) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }

    public void setBestAcq(int id) {
        if (acquaintance.size() == 1) {
            bestAcq = id;
        } else {
            if (value.get(id) > value.get(bestAcq) ||
                    (id < bestAcq && value.get(id).equals(value.get(bestAcq)))) {
                bestAcq = id;
            }
        }
    }

    public void setBestAcq2(int id, int val) {
        if (bestAcq != id) {
            if (value.get(id) > value.get(bestAcq) ||
                    (id < bestAcq && value.get(id).equals(value.get(bestAcq)))) {
                bestAcq = id;
            }
        } else if (val < 0) {
            int flag = 0;
            for (Integer x : acquaintance.keySet()) {
                if (flag == 0) {
                    bestAcq = x;
                    flag = 1;
                } else {
                    if (value.get(x) > value.get(bestAcq) ||
                            (x < bestAcq && value.get(x).equals(value.get(bestAcq)))) {
                        bestAcq = x;
                    }
                }
            }
        }
    }

    public void setBestAcq3(int id) {
        if (bestAcq == id) {
            int flag = 0;
            for (Integer x : acquaintance.keySet()) {
                if (flag == 0) {
                    bestAcq = x;
                    flag = 1;
                } else {
                    if (value.get(x) > value.get(bestAcq) ||
                            (x < bestAcq && value.get(x).equals(value.get(bestAcq)))) {
                        bestAcq = x;
                    }
                }
            }
        }
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }

    public int getBestAcq() {
        return bestAcq;
    }
}
