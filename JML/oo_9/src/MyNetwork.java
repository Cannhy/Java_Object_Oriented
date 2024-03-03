import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Objects;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, ArrayList<Integer>> graph;
    private HashMap<Integer, Integer> fathers;

    public MyNetwork() {
        people = new HashMap<>();
        graph = new HashMap<>();
        fathers = new HashMap<>();
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

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ((MyPerson) person1).getAcquaintance().put(id2, person2);
            ((MyPerson) person1).getValue().put(id2, value);
            ((MyPerson) person2).getAcquaintance().put(id1, person1);
            ((MyPerson) person2).getValue().put(id1, value);
            merge(id1, id2);
            if (graph.containsKey(id1)) {
                graph.get(id1).add(id2);
            } else {
                ArrayList<Integer> te = new ArrayList<>();
                te.add(id2);
                graph.put(id1, te);
            }
            if (graph.containsKey(id2)) {
                graph.get(id2).add(id1);
            } else {
                ArrayList<Integer> te = new ArrayList<>();
                te.add(id1);
                graph.put(id2, te);
            }
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
            int fatherId1 = find(id1);
            int fatherId2 = find(id2);
            return fatherId1 == fatherId2;
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
        while (!queue.isEmpty()) {
            int topId = queue.poll();
            for (Integer integer : ((MyPerson) getPerson(topId)).getAcquaintance().keySet()) {
                if (!st.get(integer)) {
                    if (integer == id2) {
                        return true;
                    }
                    queue.add(integer);
                    st.put(integer, true);
                }
            }
        }
        return false;
    }

    public int queryBlockSum() { // 求连通块
        int blockSum = 0;
        for (Integer id : fathers.keySet()) {
            if (fathers.get(id).equals(id)) {
                blockSum++;
            }
        }
        return blockSum;
    }

    public int queryTripleSum() { // 求三元环
        int tripleSum = 0;
        HashMap<Integer, Integer> vis = new HashMap<>();
        for (Integer v : graph.keySet()) {
            for (Integer ary : graph.get(v)) {
                if (graph.get(v).size() > graph.get(ary).size()
                        || (graph.get(v).size() == graph.get(ary).size() && v > ary)) {
                    vis.put(ary, v);
                }
            }
            for (Integer ary : graph.get(v)) {
                if (graph.get(v).size() > graph.get(ary).size()
                        || (graph.get(v).size() == graph.get(ary).size() && v > ary)) {
                    for (Integer ary2 : graph.get(ary)) {
                        if (graph.get(ary).size() > graph.get(ary2).size() ||
                                (graph.get(ary).size() == graph.get(ary2).size() && ary > ary2)) {
                            if (vis.containsKey(ary2) && Objects.equals(vis.get(ary2), v)) {
                                tripleSum++;
                            }
                        }
                    }
                }
            }
        }
        return tripleSum;
    }

    public boolean queryTripleSumOKTest(
            HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData,
            int result) {
        if (beforeData.equals(afterData)) {
            for (Integer key : beforeData.keySet()) {
                ArrayList<Integer> tem = new ArrayList<>(beforeData.get(key).keySet());
                graph.put(key, tem);
            }
            int finalRes = 0;
            finalRes = queryTripleSum();
            return finalRes == result;
        }
        return false;
    }
}