public class Order {
    private int date;
    private String bookNumber;
    private String student;
    private boolean useLess;
    private String[] req;
    private int type;

    public Order(String[] req, int date) {
        this.date = date;
        this.bookNumber = req[3];
        this.student = req[1];
        this.useLess = false;
        this.req = req;
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

    public String[] getReq() {
        return req;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
