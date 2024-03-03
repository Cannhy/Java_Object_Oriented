import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private HashMap<Integer, Integer> fathers;
    private HashSet<Integer> emojiIdList;
    private HashMap<Integer, Integer> emojiHeatList;
    private int tripleSum;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        fathers = new HashMap<>();
        emojiIdList = new HashSet<>();
        emojiHeatList = new HashMap<>();
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
        return MyNetwork2.bfs(id1, id2, fathers, people);
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
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (!containsMessage(message.getId()) && (message instanceof EmojiMessage)
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (!containsMessage(message.getId()) && (!(message instanceof EmojiMessage) ||
                containsEmojiId(((EmojiMessage) message).getEmojiId())) && message.getType() == 0
                && message.getPerson1().getId() == message.getPerson2().getId()) {
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
            Message message = getMessage(id);
            message.getPerson1().addSocialValue(message.getSocialValue());
            message.getPerson2().addSocialValue(message.getSocialValue());
            if (message instanceof RedEnvelopeMessage) {
                message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
            }
            else if (message instanceof EmojiMessage) {
                if (containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                    Integer old = emojiHeatList.get(((EmojiMessage) message).getEmojiId());
                    emojiHeatList.put(((EmojiMessage) message).getEmojiId(), old + 1);
                }
            }
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id));
            messages.remove(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 1 &&
                getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1())) {
            Message message = getMessage(id);
            for (Person person : ((MyGroup) (message.getGroup())).getPeople().values()) {
                person.addSocialValue(message.getSocialValue());
            }
            if (message instanceof RedEnvelopeMessage) {
                int i = ((RedEnvelopeMessage) message).getMoney() / message.getGroup().getSize();
                message.getPerson1().addMoney(-(i * (message.getGroup().getSize() - 1)));
                for (Person person : ((MyGroup) (message.getGroup())).getPeople().values()) {
                    if (person.getId() != message.getPerson1().getId()) {
                        person.addMoney(i);
                    }
                }
            } else if (message instanceof EmojiMessage) {
                if (containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                    Integer old = emojiHeatList.get(((EmojiMessage) message).getEmojiId());
                    emojiHeatList.put(((EmojiMessage) message).getEmojiId(), old + 1);
                }
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
        return MyNetwork2.qcs(people);
    }

    @Override
    public boolean containsEmojiId(int id) {
        return emojiIdList.contains(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            MyNetwork2.se(id, emojiIdList, emojiHeatList);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getMoney();
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emojiHeatList.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        return MyNetwork2.dce(limit, emojiIdList, emojiHeatList, messages);
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            getPerson(personId).getMessages().removeIf(tem -> tem instanceof NoticeMessage);
        }
    }

    @Override
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            int ans = MyNetwork2.qlm(id, people);
            if (ans == Integer.MAX_VALUE) {
                throw new MyPathNotFoundException(id);
            } else {
                return ans;
            }
        }
    }

    @Override
    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        return MyNetwork2.okTest(limit, beforeData, afterData, result);
    }
}