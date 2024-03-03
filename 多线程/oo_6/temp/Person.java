public class Person {
    private final int id;
    private int src;
    private final int des;
    private boolean isMain;

    public Person(int id, int src, int des) {
        this.id = id;
        this.src = src;
        this.des = des;
        this.isMain = false;
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
}
