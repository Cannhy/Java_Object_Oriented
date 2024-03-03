import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    private String id;
    private boolean haveB;
    private HashMap<String, Book> bookBs;
    private HashMap<String, Book> bookCs;
    private HashMap<String, HashMap<String, Book>> orders;
    private ArrayList<String> orderings;
    private HashMap<String, Boolean> isSmeared;

    public Student(String id) {
        this.id = id;
        this.haveB = false;
        this.bookCs = new HashMap<>();
        this.bookBs = new HashMap<>();
        this.orders = new HashMap<>();
        this.orderings = new ArrayList<>();
        this.isSmeared = new HashMap<>();
    }

    public boolean HaveB() {
        return haveB;
    }

    public boolean IsHaveBook(String bookNumber) {
        return bookCs.containsKey(bookNumber);
    }

    public void SetB(boolean t) {
        this.haveB = t;
    }

    public HashMap<String, Book> getBookCs() {
        return bookCs;
    }

    public HashMap<String, Book> getBookBs() {
        return bookBs;
    }

    public HashMap<String, HashMap<String, Book>> getOrders() {
        return orders;
    }

    public ArrayList<String> getOrderings() {
        return orderings;
    }

    public HashMap<String, Boolean> getIsSmeared() {
        return isSmeared;
    }
}
