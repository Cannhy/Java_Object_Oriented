import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.HashSet;
import javafx.util.Pair;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private HashMap<Integer, Integer> fathers;
    private int tripleSum;
    private final HashSet<Pair<Integer, Integer>> backup;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        fathers = new HashMap<>();
        backup = new HashSet<>();
        tripleSum = 0;
    }

    private int find(int son) {
        if (fathers.get(son) == son) {
            return son;
        } else {
            int fi = find(fathers.get(son));
            fathers.put(son, fi);
            return fi;
        }
    }

    private void merge(int son1, int son2) {
        int id1 = find(son1);
        int id2 = find(son2);
        if (id1 != id2) {
            fathers.put(id2, id1);
        }
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return contains(id) ? people.get(id) : null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (person != null) {
            if (contains(person.getId())) {
                throw new MyEqualPersonIdException(person.getId());
            } else {
                people.put(person.getId(), person);
                fathers.put(person.getId(), person.getId());
            }
        }
    }

    public void serveTri(int id1, int id2, int type) {
        int v = ((MyPerson) getPerson(id1)).getAcquaintance().size()
                < ((MyPerson) getPerson(id2)).getAcquaintance().size() ? id1 : id2;
        int u = (v == id1) ? id2 : id1;
        Person temp = getPerson(v);
        for (Person item : ((MyPerson) temp).getAcquaintance().values()) {
            if (item.isLinked(getPerson(u))) {
                if (type == 1) {
                    tripleSum++;
                } else {
                    tripleSum--;
                }
            }
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            serveTri(id1, id2, 1);
            ((MyPerson) person1).getAcquaintance().put(id2, person2);
            ((MyPerson) person1).getValue().put(id2, value);
            ((MyPerson) person2).getAcquaintance().put(id1, person1);
            ((MyPerson) person2).getValue().put(id1, value);
            ((MyPerson) person1).setBestAcq(id2);
            ((MyPerson) person2).setBestAcq(id1);
            merge(id1, id2);
            modGroupVal(id1, id2, value);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            if (id1 == id2) {
                return true;
            }
            int f1 = find(id1);
            int f2 = find(id2);
            return f1 == f2;
        }
    }

    public int queryBlockSum() { // 求连通块
        int blockSum = 0;
        for (Integer x : fathers.keySet()) {
            if (fathers.get(x).equals(x)) {
                blockSum++;
            }
        }
        return blockSum;
    }

    public int queryTripleSum() { // 求三元环
        return tripleSum;
    }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            if (getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
                int old = person1.queryValue(person2);
                ((MyPerson) person1).getValue().put(id2, value + old);
                ((MyPerson) person2).getValue().put(id1, value + old);
                ((MyPerson) person1).setBestAcq2(id2, value);
                ((MyPerson) person2).setBestAcq2(id1, value);
                modGroupVal(id1, id2, value);
            } else {
                modGroupVal(id1, id2, person1.queryValue(person2) * (-1));
                ((MyPerson) person1).getAcquaintance().remove(id2);
                ((MyPerson) person1).getValue().remove(id2);
                ((MyPerson) person2).getAcquaintance().remove(id1);
                ((MyPerson) person2).getValue().remove(id1);
                if (!bfs(id1, id2)) {
                    fathers.put(id1, id1);
                    fathers.put(id2, id2);
                    bfsMerge(id2);
                }
                ((MyPerson) person1).setBestAcq3(id2);
                ((MyPerson) person2).setBestAcq3(id1);
                serveTri(id1, id2, 0);
            }
        }
    }

    public void modGroupVal(int id1, int id2, int value) {
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        for (Integer key : ((MyPerson) person1).getGroups().keySet()) {
            if (((MyPerson) person2).getGroups().containsKey(key)) {
                ((MyGroup) ((MyPerson) person1).getGroups().get(key)).addValueSum(2 * value);
            }
        }
    }

    public void bfsMerge(int id) {
        Queue<Integer> queue = new LinkedList<>();
        HashMap<Integer, Boolean> st = new HashMap<>();
        for (Integer ida : people.keySet()) {
            st.put(ida, false);
        }
        queue.add(id);
        st.put(id, true);
        while (!queue.isEmpty()) {
            int topId = queue.poll();
            for (Integer integer : ((MyPerson) getPerson(topId)).getAcquaintance().keySet()) {
                if (!st.get(integer)) {
                    queue.add(integer);
                    st.put(integer, true);
                    fathers.put(integer, id);
                }
            }
        }
    }

    public boolean bfs(int id1, int id2) {
        if (id1 == id2) {
            return true;
        }
        Queue<Integer> queue = new LinkedList<>();
        HashMap<Integer, Boolean> st = new HashMap<>();
        for (Integer id : people.keySet()) {
            st.put(id, false);
        }
        queue.add(id1);
        st.put(id1, true);
        int flag = 0;
        fathers.put(id1, id1);
        while (!queue.isEmpty()) {
            int topId = queue.poll();
            for (Integer integer : ((MyPerson) getPerson(topId)).getAcquaintance().keySet()) {
                if (!st.get(integer)) {
                    if (integer == id2) {
                        flag = 1;
                    }
                    queue.add(integer);
                    st.put(integer, true);
                    fathers.put(integer, id1);
                }
            }
        }
        return (flag == 1);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (group == null) {
            return;
        }
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else if (getGroup(id2).getSize() <= 1111) {
            getGroup(id2).addPerson(getPerson(id1));
            ((MyPerson) getPerson(id1)).getGroups().put(id2, getGroup(id2));
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getValueSum();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getAgeVar();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            getGroup(id2).delPerson(getPerson(id1));
            ((MyPerson) getPerson(id1)).getGroups().remove(id2);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws
            EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getId() == 0 &&
                message.getPerson1().getId() == message.getPerson2().getId()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(), message);
        }
    }

    @Override
    public Message getMessage(int id) {
        if (!containsMessage(id)) {
            return null;
        }
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else if (containsMessage(id) && getMessage(id).getType() == 0 &&
                getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
                getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
            getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id));
            messages.remove(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 1 &&
                getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1())) {
            for (Person person : ((MyGroup) (getMessage(id).getGroup())).getPeople().values()) {
                person.addSocialValue(getMessage(id).getSocialValue());
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        }
        throw new MyPersonIdNotFoundException(id);
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else if (((MyPerson) getPerson(id)).getAcquaintance().size() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            return ((MyPerson) getPerson(id)).getBestAcq();
        }
    }

    @Override
    public int queryCoupleSum() {
        HashMap<Integer, Boolean> queue = new HashMap<>();
        for (Integer x : people.keySet()) {
            queue.put(x, false);
        }
        int cnt = 0;
        for (Integer id : queue.keySet()) {
            if (((MyPerson) getPerson(id)).getAcquaintance().size() > 0) {
                if (!queue.get(id)) {
                    int temp = ((MyPerson) getPerson(id)).getBestAcq();
                    if (((MyPerson) getPerson(temp)).getAcquaintance().size() > 0
                            && ((MyPerson) getPerson(temp)).getBestAcq() == id) {
                        cnt++;
                        queue.put(id, true);
                        queue.put(temp, true);
                    }
                }
            }
        }
        return cnt;
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
                || id1 == id2 || !beforeData.get(id1).containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            }
            return -1;
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        }
        if (!ok2(beforeData, afterData)) {
            return 2;
        }
        if (!ok3(id1, id2, beforeData, afterData)) {
            return 3;
        }
        if (beforeData.get(id1).get(id2) + value > 0) {
            return OkTest.test1(id1, id2, value, beforeData, afterData);
        }
        return OkTest.test2(id1, id2, value, beforeData, afterData);
    }

    public boolean ok2(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer i : beforeData.keySet()) {
            if (!afterData.containsKey(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean ok3(int id1, int id2,
                       HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer i : beforeData.keySet()) {
            if (i != id1 && i != id2) {
                if (!beforeData.get(i).equals(afterData.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}