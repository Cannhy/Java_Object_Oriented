import java.util.ArrayList;

public class Person {
    private final int id;
    private int src;
    private int des;
    private int finalDes;
    private boolean isMain;
    private ArrayList<Integer> nextDes;

    public Person(int id, int src, int des) {
        this.id = id;
        this.src = src;
        this.des = des;
        this.finalDes = des;
        this.isMain = false;
        this.nextDes = new ArrayList<>();
    }

    public ArrayList<Integer> getNextDes() {
        return nextDes;
    }

    public void setNextDes(ArrayList<Integer> nextDes) {
        this.nextDes = nextDes;
    }

    public void setDes() {
        this.des = nextDes.get(0);
        nextDes.remove(0);
    }

    public int getId() {
        return id;
    }

    public int getSrc() {
        return src;
    }

    public int getDes() {
        return des;
    }

    public boolean getMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getFinalDes() {
        return finalDes;
    }
}
