import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Department {
    private HashMap<String, Library> librarys;
    private ArrayList<String> seqLibs;
    private ArrayList<String> nowReq;
    private int[] enumDay;
    private int lastArrangeDay;
    private int today;
    private ArrayList<String> otherOrders;
    private ArrayList<Book> toTranBooks = new ArrayList<>();
    private ArrayList<Book> fromTranBooks = new ArrayList<>();
    private ArrayList<Book> receiveBooks = new ArrayList<>();

    public Department() {
        librarys = new HashMap<>();
        seqLibs = new ArrayList<>();
        this.nowReq = new ArrayList<>();
        enumDay = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        this.lastArrangeDay = 1;
        this.otherOrders = new ArrayList<>();
    }

    public void addBooks(Scanner scanner, int t) {
        for (int i = 0; i < t; i++) {
            String temp = scanner.nextLine();
            String[] temp2 = temp.split(" ");
            Library library = new Library(this, temp2[0]);
            librarys.put(temp2[0], library);
            seqLibs.add(temp2[0]);
            int n = Integer.parseInt(temp2[1]);
            for (int j = 0; j < n; j++) {
                library.addBook(scanner.nextLine(), temp2[0]);
            }
        }
    }

    public void run(Scanner scanner, int m) {
        String last = "";
        for (int i = 0; i < m; i++) {
            String req = scanner.nextLine();
            String[] reqSplit = req.split(" ");
            if (last.equals("")) {
                last = reqSplit[0].substring(1, 11);
                today = Date2Day(last);
                nowReq.add(req);
            } else {
                if (!last.equals(reqSplit[0].substring(1, 11))) {
                    today = Date2Day(last);
                    arrangeDay();
                    dealTodayMatter();
                    dealOtherReq();
                    close2outToday();
                    today += 1;
                    close2outTomo();
                    last = reqSplit[0].substring(1, 11);
                    nowReq.add(req);
                } else {
                    nowReq.add(req);
                }
            }
        }
        today = Date2Day(last);
        arrangeDay();
        dealTodayMatter();
        dealOtherReq();
        close2outToday();
    }

    public void dealTodayMatter() {
        for (int i = 0; i < nowReq.size(); i++) {
            String req = nowReq.get(i);
            String[] reqSplit = req.split(" ");
            String school = reqSplit[1].split("-")[0];
            switch (reqSplit[2]) {
                case "borrowed":
                    librarys.get(school).dealBorrowed(reqSplit, req);
                    break;
                case "smeared":
                    librarys.get(school).dealSmeared(reqSplit);
                    break;
                case "lost":
                    librarys.get(school).dealLost(reqSplit);
                    break;
                case "returned":
                    librarys.get(school).dearReturn(reqSplit);
                    break;
                default:
                    break;
            }
            nowReq.remove(i);
            i--;
        }
    }

    public void close2outToday() {
        for (String schoolName : seqLibs) {
            Library library = librarys.get(schoolName);
            library.close2order();
        }
        for (Book src : toTranBooks) {
            System.out.println("[" + Day2Date(today) + "] " + src.getSchool() + "-"
                    + src.getBookNumber() + " got transported by purchasing department in "
                    + src.getSrc());
        }
        for (Book src : receiveBooks) {
            System.out.println("[" + Day2Date(today) + "] " + src.getSchool()
                    + "-" + src.getBookNumber()
                    + " got transported by purchasing department in "
                    + src.getSrc());
        }
        toTranBooks.clear();
    }

    public void close2outTomo() {
        String tomorrow = Day2Date(today);
        for (Book src : fromTranBooks) {
            System.out.println("[" + tomorrow + "] " + src.getSchool() + "-" + src.getBookNumber()
                    + " got received by purchasing department in "
                    + src.getDes());
        }
        for (Book src : receiveBooks) {
            System.out.println("[" + tomorrow + "] " + src.getSchool() + "-" + src.getBookNumber()
                    + " got received by purchasing department in "
                    + src.getSchool());
            src.setStoreDay(today);
            librarys.get(src.getSchool()).getStoreBooks().add(src);
        }
        for (Book src : fromTranBooks) {
            System.out.println("[" + tomorrow + "] purchasing department lent " +
                    src.getSchool() + "-" + src.getBookNumber() + " to " + src.getMaster());
            System.out.println("[" + tomorrow + "] " + src.getMaster() + " borrowed "
                    + src.getSchool() + "-"
                    + src.getBookNumber() + " from purchasing department");
            if (src.isBtype()) {
                librarys.get(src.getDes()).getStudents().get(src.getMaster())
                        .getBookBs().put(src.getBookNumber(), src);
                librarys.get(src.getDes()).getStudents().get(src.getMaster()).SetB(true);
                librarys.get(src.getDes()).getStudents().get(src.getMaster())
                        .getIsSmeared().put(src.getBookNumber(), false);
                librarys.get(src.getDes()).clearB2(src.getMaster());
            } else {
                librarys.get(src.getDes()).getStudents().get(src.getMaster())
                        .getBookCs().put(src.getBookNumber(), src);
                librarys.get(src.getDes()).getStudents().get(src.getMaster())
                        .getIsSmeared().put(src.getBookNumber(), false);
            }
        }
        fromTranBooks.clear();
        receiveBooks.clear();
    }

    public void dealB(String[] reqSpl, String req) {
        if (!librarys.get(reqSpl[1].split("-")[0]).getStudents().
                get(reqSpl[1]).HaveB()) {
            String t = judgeIsBorrow(req);
            if (t.equals("")) {
                if (librarys.get(reqSpl[1].split("-")[0])
                        .getBooks().containsKey(reqSpl[3])) {
                    Order order = new Order(reqSpl, Date2Day(reqSpl[0].substring(1, 11)));
                    order.setType(0);
                    librarys.get(reqSpl[1].split("-")[0]).getToOrderBooks().add(order);
                } else {
                    Order order = new Order(reqSpl, Date2Day(reqSpl[0].substring(1, 11)));
                    //librarys.get(reqSpl[1].split("-")[0]).getOrders().add(order);
                    librarys.get(reqSpl[1].split("-")[0]).getToOrderBooks().add(order);
                    librarys.get(reqSpl[1].split("-")[0]).getBuyBooks().add(order);
                }
            } else {
                //Book book = librarys.get(t).getBooks().get(reqSpl[3]);
                if (!librarys.get(reqSpl[1].split("-")[0]).getStudents().get(reqSpl[1]).HaveB()) {
                    Book book1 = new Book(reqSpl[3]);
                    book1.setMaster(reqSpl[1]);
                    book1.setDes(reqSpl[1].split("-")[0]);
                    book1.setSrc(t);
                    book1.setSchool(t);
                    toTranBooks.add(book1);
                    fromTranBooks.add(book1);
                    int cnt = librarys.get(t).getBookCounter().get(reqSpl[3]);
                    librarys.get(t).getBookCounter().put(reqSpl[3], cnt - 1);
                    librarys.get(reqSpl[1].split("-")[0]).getStudents().get(reqSpl[1]).SetB(true);
                }
            }
        }
    }

    public void dealOtherReq() {
        Iterator<String> iter = otherOrders.iterator();
        while (iter.hasNext()) {
            String req = iter.next();
            String[] reqSpl = req.split(" ");
            if  (reqSpl[3].charAt(0) == 'B') {
                dealB(reqSpl, req);
            } else {
                if (!librarys.get(reqSpl[1].split("-")[0]).getStudents().
                        get(reqSpl[1]).IsHaveBook(reqSpl[3])) {
                    String t = judgeIsBorrow(req);
                    if (t.equals("")) {
                        if (librarys.get(reqSpl[1].split("-")[0]).getBooks()
                                .containsKey(reqSpl[3])) {
                            Order order = new Order(reqSpl, Date2Day(reqSpl[0].substring(1, 11)));
                            order.setType(0);
                            librarys.get(reqSpl[1].split("-")[0]).getToOrderBooks().add(order);
                        } else {
                            Order order = new Order(reqSpl, Date2Day(reqSpl[0].substring(1, 11)));
                            order.setType(1);
                            librarys.get(reqSpl[1].split("-")[0]).getToOrderBooks().add(order);
                            //librarys.get(reqSpl[1].split("-")[0]).getOrders().add(order);
                            librarys.get(reqSpl[1].split("-")[0]).getBuyBooks().add(order);
                        }
                    } else {
                        //Book book = librarys.get(t).getBooks().get(reqSpl[3]);
                        Book book1 = new Book(reqSpl[3]);
                        book1.setMaster(reqSpl[1]);
                        book1.setDes(reqSpl[1].split("-")[0]);
                        book1.setSrc(t);
                        book1.setSchool(t);
                        toTranBooks.add(book1);
                        fromTranBooks.add(book1);
                        int cnt = librarys.get(t).getBookCounter().get(reqSpl[3]);
                        librarys.get(t).getBookCounter().put(reqSpl[3], cnt - 1);
                    }
                }
            }
            iter.remove();
        }
    }

    public void arrangeDay() {
        while (lastArrangeDay <= today) {
            for (String school : seqLibs) {
                librarys.get(school).buyNewBook(lastArrangeDay);
            }
            System.out.println("[" + Day2Date(lastArrangeDay)
                    + "] arranging librarian arranged all the books");
            for (String school : seqLibs) {
                librarys.get(school).allocateBook(lastArrangeDay);
            }
            lastArrangeDay += 3;
        }
    }

    public String judgeIsBorrow(String req) {
        String[] sp = req.split(" ");
        for (String name : librarys.keySet()) {
            Library library = librarys.get(name);
            if (library.getBooks().containsKey(sp[3]) && library.getBookCounter().get(sp[3]) > 0) {
                if (library.getLentBooks().get(sp[3])) {
                    return name;
                }
            }
        }
        return "";
    }

    public void borrowFromOther(String req) {
        otherOrders.add(req);
    }

    public int Date2Day(String req) {
        int month = Integer.parseInt(req.substring(5, 7));
        int day = Integer.parseInt(req.substring(8, 10));
        int res = 0;
        for (int i = 1; i <= month; i++) {
            if (i == month) {
                res += day;
            } else {
                res += enumDay[i];
            }
        }
        return res;
    }

    public String Day2Date(int day) {
        StringBuilder res = new StringBuilder("2023-");
        int temp = 0;
        int month = 1;
        int day2 = 0;
        for (int i = 1; i <= 12; i++) {
            if (enumDay[i] + temp < day) {
                month += 1;
                temp += enumDay[i];
            } else if (enumDay[i] + temp == day) {
                day2 = enumDay[i];
                break;
            } else {
                day2 = day - temp;
                break;
            }
        }
        if (month < 10) {
            res.append("0").append(month).append("-");
        } else {
            res.append(month).append("-");
        }
        if (day2 < 10) {
            res.append("0").append(day2);
        } else {
            res.append(day2);
        }
        return res.toString();
    }

    public ArrayList<Book> getReceiveBooks() {
        return receiveBooks;
    }
}
