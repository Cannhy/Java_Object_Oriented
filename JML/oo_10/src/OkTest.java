import java.util.HashMap;

public class OkTest {
    public static int test1(int id1, int id2, int value,
                     HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                     HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!ok4(id1, id2, beforeData, afterData)) {
            return 4;
        }
        if (!ok5(id1, id2, value, beforeData, afterData)) {
            return 5;
        }
        if (!ok6(id1, id2, value, beforeData, afterData)) {
            return 6;
        }
        if (!ok7(id1, beforeData, afterData)) {
            return 7;
        }
        if (!ok8(id2, beforeData, afterData)) {
            return 8;
        }
        if (!ok9(id1, beforeData, afterData)) {
            return 9;
        }
        if (!ok10(id2, beforeData, afterData)) {
            return 10;
        }
        if (!ok11(id1, id2, beforeData, afterData)) {
            return 11;
        }
        if (!ok12(id1, id2, beforeData, afterData)) {
            return 12;
        }
        return 0;
    }

    public static int test2(int id1, int id2, int value,
                     HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                     HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!ok15(id1, id2, afterData)) {
            return 15;
        }
        if (!ok16(id1,beforeData, afterData)) {
            return 16;
        }
        if (!ok17(id2, beforeData, afterData)) {
            return 17;
        }
        if (!ok20(id1, beforeData, afterData)) {
            return 20;
        }
        if (!ok21(id2, beforeData, afterData)) {
            return 21;
        }
        return 0;
    }

    public static boolean ok4(int id1, int id2,
                       HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id1).containsKey(id2) && afterData.get(id2).containsKey(id1);
    }

    public static boolean ok5(int id1, int id2, int value,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id1).get(id2) == beforeData.get(id1).get(id2) + value;
    }

    public static boolean ok6(int id1, int id2, int value,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id2).get(id1) == beforeData.get(id2).get(id1) + value;
    }

    public static boolean ok7(int id1,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id1).size() == beforeData.get(id1).size();
    }

    public static boolean ok8(int id2,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id2).size() == beforeData.get(id2).size();
    }

    public static boolean ok9(int id1,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id1).keySet().equals(beforeData.get(id1).keySet());
    }

    public static boolean ok10(int id2,
                              HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                              HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return afterData.get(id2).keySet().equals(beforeData.get(id2).keySet());
    }

    public static boolean ok11(int id1, int id2,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer x : afterData.get(id1).keySet()) {
            if (x != id2) {
                if (!afterData.get(id1).get(x).equals(beforeData.get(id1).get(x))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ok12(int id1, int id2,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer x : afterData.get(id2).keySet()) {
            if (x != id1) {
                if (!afterData.get(id2).get(x).equals(beforeData.get(id2).get(x))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ok15(int id1, int id2,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return !afterData.get(id1).containsKey(id2) && !afterData.get(id2).containsKey(id1);
    }

    public static boolean ok16(int id1,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return (beforeData.get(id1).size() == (afterData.get(id1).size() + 1));
    }

    public static boolean ok17(int id2,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return (beforeData.get(id2).size() == (afterData.get(id2).size() + 1));
    }

    public static boolean ok20(int id1,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer key : afterData.get(id1).keySet()) {
            if (!beforeData.get(id1).containsKey(key)) {
                return false;
            }
            if (!beforeData.get(id1).get(key).equals(afterData.get(id1).get(key))) {
                return false;
            }
        }
        return true;
    }

    public static boolean ok21(int id2,
                               HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                               HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        for (Integer key : afterData.get(id2).keySet()) {
            if (!beforeData.get(id2).containsKey(key)) {
                return false;
            }
            if (!beforeData.get(id2).get(key).equals(afterData.get(id2).get(key))) {
                return false;
            }
        }
        return true;
    }
}
