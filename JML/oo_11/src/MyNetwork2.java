import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;

public class MyNetwork2 {
    private static HashMap<Integer, Person> people;
    private static final HashMap<Integer, Integer> res = new HashMap<>();

    public static int okTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                             ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        int cnt = 0;
        for (Integer inte : beforeData.get(0).keySet()) {
            if (beforeData.get(0).get(inte) >= limit) {
                cnt++;
                if (!afterData.get(0).containsKey(inte)) {
                    return 1;
                }
            }
        }
        for (Integer inte : afterData.get(0).keySet()) {
            if (!((beforeData.get(0).containsKey(inte)) &&
                    (beforeData.get(0).get(inte).equals(afterData.get(0).get(inte))))) {
                return 2;
            }
        }
        if (afterData.get(0).keySet().size() != cnt) {
            return 3;
        }
        if (afterData.get(0).keySet().size() != afterData.get(0).values().size()) {
            return 4;
        }
        for (Integer x : beforeData.get(1).keySet()) {
            if ((beforeData.get(1).get(x) != null) &&
                    (afterData.get(0).containsKey(beforeData.get(1).get(x)))) {
                if (!(afterData.get(1).containsKey(x) &&
                        afterData.get(1).get(x).equals(beforeData.get(1).get(x)))) {
                    return 5;
                }
            }
        }
        for (Integer x : beforeData.get(1).keySet()) {
            if (beforeData.get(1).get(x) == null) {
                if (!(afterData.get(1).containsKey(x) && afterData.get(1).get(x) == null)) {
                    return 6;
                }
            }
        }
        int cnt2 = 0;
        for (Integer x : beforeData.get(1).keySet()) {
            if (beforeData.get(1).get(x) != null) {
                if (afterData.get(0).containsKey(beforeData.get(1).get(x))) {
                    cnt2++;
                }
            } else {
                cnt2++;
            }
        }
        if (afterData.get(1).keySet().size() != cnt2) {
            return 7;
        }
        if (!(result == afterData.get(0).keySet().size())) {
            return 8;
        }
        return 0;
    }

    public static void se(int id, HashSet<Integer> emojiIdList,
                          HashMap<Integer, Integer> emojiHeatList) {
        emojiIdList.add(id);
        emojiHeatList.put(id, 0);
    }

    public static int qcs(HashMap<Integer, Person> people) {
        HashMap<Integer, Boolean> queue = new HashMap<>();
        for (Integer x : people.keySet()) {
            queue.put(x, false);
        }
        int cnt = 0;
        for (Integer id : queue.keySet()) {
            if (((MyPerson) getPerson(id, people)).getAcquaintance().size() > 0) {
                if (!queue.get(id)) {
                    int temp = ((MyPerson) getPerson(id, people)).getBestAcq();
                    if (((MyPerson) getPerson(temp, people)).getAcquaintance().size() > 0
                            && ((MyPerson) getPerson(temp, people)).getBestAcq() == id) {
                        cnt++;
                        queue.put(id, true);
                        queue.put(temp, true);
                    }
                }
            }
        }
        return cnt;
    }

    public static Person getPerson(int id, HashMap<Integer, Person> people) {
        return contains(id, people) ? people.get(id) : null;
    }

    public static boolean contains(int id, HashMap<Integer, Person> people) {
        return people.containsKey(id);
    }

    public static int dce(int limit, HashSet<Integer> emojiIdList,
                          HashMap<Integer, Integer> emojiHeatList,
                          HashMap<Integer, Message> messages) {
        Iterator<Integer> eiter = emojiIdList.iterator();
        while (eiter.hasNext()) {
            Integer temp = eiter.next();
            if (emojiHeatList.get(temp) < limit) {
                eiter.remove();
                emojiHeatList.remove(temp);
            }
        }
        Iterator<Map.Entry<Integer, Message>> miter = messages.entrySet().iterator();
        while (miter.hasNext()) {
            Map.Entry<Integer, Message> temp = miter.next();
            Message message = temp.getValue();
            if ((message instanceof EmojiMessage) &&
                    (!containsEmojiId(((EmojiMessage) message).getEmojiId(), emojiIdList))) {
                miter.remove();
            }
        }
        return emojiIdList.size();
    }

    public static boolean containsEmojiId(int id, HashSet<Integer> emojiIdList) {
        return emojiIdList.contains(id);
    }

    public static boolean bfs(int id1, int id2,
                              HashMap<Integer, Integer> fathers, HashMap<Integer, Person> people) {
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
            for (Integer integer :
                    ((MyPerson) getPerson(topId, people)).getAcquaintance().keySet()) {
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

    private static void init(HashMap<Integer, Integer> dis, HashMap<Integer, Boolean> vis,
                                int ori, HashMap<Integer, Integer> branch,
                                HashMap<Integer, Integer> len, PriorityQueue<Graph> heap) {
        MyPerson origin = (MyPerson) people.get(ori);
        int idx = 0;
        for (Integer i : people.keySet()) {
            vis.put(i, false);
            if (origin.isLinked(people.get(i)) && !i.equals(ori)) {
                dis.put(i, origin.queryValue(people.get(i)));
                branch.put(i, ++idx);
                res.put(i, ori);
                len.put(i, 1);
                heap.add(new Graph(ori, i, origin.queryValue(people.get(i))));
            } else {
                dis.put(i, Integer.MAX_VALUE);
                branch.put(i, 0);
                len.put(i, 0);
            }
        }
    }

    public static HashMap<Integer, Integer> dijsktra(int des) {
        HashMap<Integer, Integer> lengt = new HashMap<>();
        PriorityQueue<Graph> pq = new PriorityQueue<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        HashMap<Integer, Integer> branch = new HashMap<>();
        init(dist, visited, des, branch, lengt, pq);
        visited.put(des, true);
        while (!pq.isEmpty()) {
            Graph cur = pq.poll();
            int curId = cur.getTo();
            if (visited.get(curId)) {
                continue;
            }
            visited.put(curId, true);
            MyPerson person = (MyPerson) people.get(curId);
            for (Integer friendId : person.getAcquaintance().keySet()) {
                if (branch.get(friendId) != 0 &&
                        !Objects.equals(branch.get(friendId), branch.get(curId)) ||
                        lengt.get(curId) >= 2 && friendId.equals(des)) {
                    int road;
                    if (dist.get(curId) != Integer.MAX_VALUE && friendId.equals(des) &&
                            ((road = ((dist.get(curId) +
                                    people.get(curId).queryValue(people.get(friendId))))) <
                                    dist.get(des))) {
                        dist.put(des, road);
                    } else if (!friendId.equals(des) &&
                            (road = (dist.get(curId) + people.get(curId)
                                    .queryValue(people.get(friendId)) + dist.get(friendId))) <
                                    dist.get(des)) {
                        dist.put(des, road);
                    }
                }
                int w = person.queryValue(people.get(friendId));
                if (dist.get(curId) + w < dist.get(friendId)) {
                    if (friendId.equals(des)) {
                        continue;
                    }
                    res.put(friendId, curId);
                    dist.put(friendId, dist.get(curId) + w);
                    pq.add(new Graph(curId, friendId, dist.get(friendId)));
                    lengt.put(friendId, lengt.get(curId) + 1);
                    branch.put(friendId, branch.get(curId));
                }
            }
        }
        return dist;
    }

    public static int qlm(int id, HashMap<Integer, Person> people) {
        MyNetwork2.people = people;
        res.clear();
        HashMap<Integer, Integer> res = dijsktra(id);
        return res.get(id);
    }
}

