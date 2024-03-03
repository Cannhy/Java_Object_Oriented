public class Book {
    private String bookNumber;
    private boolean isSmeared;
    private int storeDay;
    private String school;
    private String des;
    private String src;
    private String master;

    public Book(String bookNumber) {
        this.bookNumber = bookNumber;
        this.isSmeared = false;
        this.storeDay = 0;
    }

    public void setSmeared(boolean smeared) {
        isSmeared = smeared;
    }

    public int getStoreDay() {
        return storeDay;
    }

    public void setStoreDay(int storeDay) {
        this.storeDay = storeDay;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public boolean isBtype() {
        return bookNumber.charAt(0) == 'B';
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
