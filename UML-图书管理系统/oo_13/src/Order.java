public class Order {
    private int date;
    private String bookNumber;
    private String student;
    private boolean useLess;

    public Order(String[] req, int date) {
        this.date = date;
        this.bookNumber = req[3];
        this.student = req[1];
        this.useLess = false;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public int getDate() {
        return date;
    }

    public String getStudent() {
        return student;
    }

    public boolean isUseLess() {
        return useLess;
    }

    public void setUseLess(boolean useLess) {
        this.useLess = useLess;
    }
}
