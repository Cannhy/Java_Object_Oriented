public class Book {
    private String bookNumber;
    private boolean isSmeared;
    private int storeDay;

    public Book(String bookNumber) {
        this.bookNumber = bookNumber;
        this.isSmeared = false;
        this.storeDay = 0;
    }

    public void setSmeared(boolean smeared) {
        isSmeared = smeared;
    }

    public boolean isSmeared() {
        return isSmeared;
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
}
