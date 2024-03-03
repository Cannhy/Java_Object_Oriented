import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private int sumValue;
    private int sumAge;
    private long sumAgeSquare;
    private HashMap<Integer, Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.sumValue = 0;
        this.sumAge = 0;
        this.sumAgeSquare = 0;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Group) && (((Group) obj).getId() == id);
    }

    @Override
    public void addPerson(Person person) {
        if (person == null) {
            return;
        }
        people.put(person.getId(), person);
        for (Person x : people.values()) {
            if (x.isLinked(person)) {
                if (x.getId() == person.getId()) {
                    sumValue += person.queryValue(x);
                } else {
                    sumValue += 2 * person.queryValue(x);
                }
            }
        }
        sumAge += person.getAge();
        sumAgeSquare += ((long) person.getAge() * (long) person.getAge());
    }

    @Override
    public boolean hasPerson(Person person) {
        if (person == null) {
            return false;
        }
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return sumValue;
    }

    public void addValueSum(int val) {
        sumValue += val;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return sumAge / people.size();
    }

    // 方差公式展开方便维护
    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        return (int) ((sumAgeSquare
                - 2 * (long) sumAge * (long) getAgeMean()
                + (long) people.size() * (long) getAgeMean() * (long) getAgeMean())
                / people.size());
    }

    @Override
    public void delPerson(Person person) {
        if (person == null) {
            return;
        }
        for (Person x : people.values()) {
            if (x.isLinked(person)) {
                if (x.getId() == person.getId()) {
                    sumValue -= x.queryValue(person);
                } else {
                    sumValue -= x.queryValue(person) * 2;
                }
            }
        }
        people.remove(person.getId());
        sumAge -= person.getAge();
        sumAgeSquare -= ((long) person.getAge() * (long) person.getAge());
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }
}