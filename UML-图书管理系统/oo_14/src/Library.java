import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Library {
    private String school;
    private HashMap<String, Book> books;
    private HashMap<String, Integer> bookCounter;
    private HashMap<String, Student> students;
    private ArrayList<Order> orders;
    private ArrayList<Book> storeBooks;
    private int[] enumDay;
    private HashMap<String, Boolean> lentBooks;
    private Department department;
    private ArrayList<Order> buyBooks = new ArrayList<>();
    private ArrayList<Order> toOrderBooks = new ArrayList<>();

    public Library(Department department1, String name) {
        this.school = name;
        this.books = new HashMap<>();
        this.bookCounter = new HashMap<>();
        this.students = new HashMap<>();
        this.orders = new ArrayList<>();
        this.storeBooks = new ArrayList<>();
        this.lentBooks = new HashMap<>();
        department = department1;
        enumDay = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    public void addBook(String in, String src) {
        String[] temp = in.split(" ");
        Book book = new Book(temp[0]);
        book.setSchool(src);
        books.put(temp[0], book);
        bookCounter.put(temp[0], Integer.parseInt(temp[1]));
        boolean b;
        b = temp[2].equals("Y");
        lentBooks.put(temp[0], b);
    }

    public void dealBorrowed(String[] req, String req2) {
        System.out.println(req[0] + " " + req[1] + " " +
                "queried " + req[3] + " from self-service machine");
        System.out.println(req[0] + " self-service machine provided information of " + req[3]);
        if (req[3].charAt(0) == 'A') {
            return;
        }
        if (!students.containsKey(req[1])) {
            Student student = new Student(req[1]);
            students.put(req[1], student);
        }
        int tempCnt;
        if (!books.containsKey(req[3])) {
            tempCnt = 0;
        } else {
            tempCnt = bookCounter.get(req[3]);
        }
        if (req[3].charAt(0) == 'B') {
            if (tempCnt != 0) {
                bookCounter.put(req[3], tempCnt - 1);
                if (!students.get(req[1]).HaveB()) {
                    students.get(req[1]).getBookBs().put(req[3], books.get(req[3]));
                    students.get(req[1]).SetB(true);
                    students.get(req[1]).getIsSmeared().put(req[3], false);
                    clearB2(req[1]);
                    System.out.println(req[0] + " borrowing and returning librarian lent "
                            + school + "-" + req[3] + " to " + req[1]);
                    System.out.println(req[0] + " " + req[1] + " borrowed " + school + "-" + req[3]
                            + " from borrowing and returning librarian");
                } else {
                    System.out.println(req[0] + " borrowing and returning librarian refused " +
                            "lending " + school + "-" + req[3] + " to " + req[1]);
                    books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
                    storeBooks.add(books.get(req[3]));
                }
            } else {
                department.borrowFromOther(req2);
            }
        } else if (req[3].charAt(0) == 'C') {
            if (tempCnt != 0) {
                bookCounter.put(req[3], tempCnt - 1);
                if (!students.get(req[1]).IsHaveBook(req[3])) {
                    students.get(req[1]).getBookCs().put(req[3], books.get(req[3]));
                    students.get(req[1]).getIsSmeared().put(req[3], false);
                    System.out.println(req[0] + " self-service machine lent "
                            + school + "-" + req[3] + " to " + req[1]);
                    System.out.println(req[0] + " " + req[1] + " borrowed " + school + "-" + req[3]
                            + " from self-service machine");
                } else {
                    System.out.println(req[0] + " self-service machine refused " +
                            "lending " + school + "-" + req[3] + " to " + req[1]);
                    books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
                    storeBooks.add(books.get(req[3]));
                }
            } else {
                department.borrowFromOther(req2);
            }
        }
    }

    public void buyNewBook(int date) {
        HashMap<String, Integer> toBuy = new HashMap<>();
        ArrayList<String> outed = new ArrayList<>();
        for (int i = 0; i < buyBooks.size(); i++) {
            Order order = buyBooks.get(i);
            if (order.getDate() < date) {
                if ((order.getBookNumber().charAt(0) == 'B' && students.get(order.getStudent())
                        .HaveB()) || (order.getBookNumber().charAt(0) == 'C' && students.
                        get(order.getStudent()).IsHaveBook(order.getBookNumber()))) {
                    buyBooks.remove(i);
                    i--;
                    continue;
                }
                if (!toBuy.containsKey(order.getBookNumber())) {
                    toBuy.put(order.getBookNumber(), 1);
                } else {
                    int old = toBuy.get(order.getBookNumber());
                    toBuy.put(order.getBookNumber(), old + 1);
                }
                if (!outed.contains(order.getBookNumber())) {
                    outed.add(order.getBookNumber());
                    System.out.println("[" + Day2Date(date) + "] " + school + "-"
                            + order.getBookNumber() +
                            " got purchased by purchasing department in " + school);
                }
                lentBooks.put(order.getBookNumber(), true);
                buyBooks.remove(i);
                i--;
            }
        }
        for (String bn : toBuy.keySet()) {
            int num = Math.max(3, toBuy.get(bn));
            for (int i = 0; i < num; i++) {
                Book book = new Book(bn);
                books.put(bn, book);
                book.setStoreDay(date);
                book.setSchool(school);
                storeBooks.add(book);
            }
        }
    }

    public void close2order() {
        Iterator<Order> iter = toOrderBooks.iterator();
        while (iter.hasNext()) {
            Order order = iter.next();
            String[] req = order.getReq();
            if (order.getType() == 0) {
                if (req[3].charAt(0) == 'B') {
                    addOrderB(req);
                } else {
                    addOrderC(req);
                }
            } else {
                Order order1 = new Order(req, Date2Day(req[0].substring(1, 11)));
                System.out.println(req[0] + " " + req[1] + " ordered " + req[1]
                        .split("-")[0] + "-" + req[3] + " from ordering librarian");
                System.out.println(req[0] + " ordering librarian recorded " +
                        req[1] + "'s order of " + req[1].split("-")[0]
                        + "-" + req[3]);
                orders.add(order1);
                getBuyBooks().add(order1);
            }
            iter.remove();
        }
    }

    public void addOrderB(String[] req) {
        Student student = students.get(req[1]);
        String date = req[0].substring(1, 11);
        if (!student.HaveB()) {
            if (!student.getOrders().containsKey(date)) {
                if (!student.getOrderings().contains(req[3])) {
                    String schoolName = req[1].split("-")[0];
                    System.out.println(req[0] + " " + req[1] + " ordered " +
                            schoolName + "-" + req[3] + " from ordering librarian");
                    System.out.println(req[0] + " ordering librarian recorded " +
                            req[1] + "'s order of " + schoolName + "-" + req[3]);
                    HashMap<String, Book> hashMap = new HashMap<>();
                    student.getOrders().put(date, hashMap);
                    student.getOrderings().add(req[3]);
                    hashMap.put(req[3], books.get(req[3]));
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                }
            } else {
                if (student.getOrders().get(date).size() < 3 &&
                        !student.getOrderings().contains(req[3])) {
                    String schoolName = req[1].split("-")[0];
                    System.out.println(req[0] + " " + req[1] + " ordered " +
                            schoolName + "-" + req[3] + " from ordering librarian");
                    System.out.println(req[0] + " ordering librarian recorded " +
                            req[1] + "'s order of " + schoolName + "-" + req[3]);
                    student.getOrders().get(date).put(req[3], books.get(req[3]));
                    student.getOrderings().add(req[3]);
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                }
            }
        }
    }

    public void addOrderC(String[] req) {
        Student student = students.get(req[1]);
        String date = req[0].substring(1, 11);
        if (!student.getBookCs().containsKey(req[3])) {
            if (!student.getOrders().containsKey(date)) {
                if (!student.getOrderings().contains(req[3])) {
                    String schoolName = req[1].split("-")[0];
                    System.out.println(req[0] + " " + req[1] + " ordered " +
                            schoolName + "-" + req[3] + " from ordering librarian");
                    System.out.println(req[0] + " ordering librarian recorded " +
                            req[1] + "'s order of " + schoolName + "-" + req[3]);
                    HashMap<String, Book> hashMap = new HashMap<>();
                    student.getOrders().put(date, hashMap);
                    student.getOrderings().add(req[3]);
                    hashMap.put(req[3], books.get(req[3]));
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                }
            } else {
                if (student.getOrders().get(date).size() < 3 &&
                        !student.getOrderings().contains(req[3])) {
                    String schoolName = req[1].split("-")[0];
                    System.out.println(req[0] + " " + req[1] + " ordered " +
                            schoolName + "-" + req[3] + " from ordering librarian");
                    System.out.println(req[0] + " ordering librarian recorded " +
                            req[1] + "'s order of " + schoolName + "-" + req[3]);
                    student.getOrders().get(date).put(req[3], books.get(req[3]));
                    student.getOrderings().add(req[3]);
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                }
            }
        }
    }

    public void dealSmeared(String[] req) {
        Student student = students.get(req[1]);
        student.getIsSmeared().put(req[3], true);
    }

    public void dealLost(String[] req) {
        System.out.println(req[0] + " " + req[1]
                + " got punished by borrowing and returning librarian");
        System.out.println(req[0] + " " + "borrowing and returning librarian received "
                + req[1] + "'s fine");
        Student student = students.get(req[1]);
        if (req[3].charAt(0) == 'B') {
            student.getBookBs().remove(req[3]);
            student.SetB(false);
        } else {
            student.getBookCs().remove(req[3]);
        }
        student.getIsSmeared().remove(req[3]);
    }

    public void dealReturnB(String[] req) {
        Student student = students.get(req[1]);
        Book book = student.getBookBs().get(req[3]);
        if (student.getIsSmeared().get(req[3])) {
            System.out.println(req[0] + " " + req[1]
                    + " got punished by borrowing and returning librarian");
            System.out.println(req[0] + " " + "borrowing and returning librarian received "
                    + req[1] + "'s fine");
            System.out.println(req[0] + " " + req[1] + " returned " + book.getSchool() + "-"
                    + req[3] + " to borrowing and returning librarian");
            System.out.println(req[0] + " borrowing and returning librarian collected " +
                    book.getSchool() + "-" + req[3] + " from " + req[1]);
            System.out.println(req[0] + " " + book.getSchool() + "-" + req[3] +
                    " got repaired by logistics division in " + school);
            book.setSmeared(false);
        } else {
            System.out.println(req[0] + " " + req[1] + " returned " + book.getSchool() + "-"
                    + req[3] + " to borrowing and returning librarian");
            System.out.println(req[0] + " borrowing and returning librarian collected " +
                    book.getSchool() + "-" + req[3] + " from " + req[1]);
        }
        if (!student.getBookBs().get(req[3]).getSchool().equals(school)) {
            student.getBookBs().get(req[3]).setSrc(school);
            department.getReceiveBooks().add(student.getBookBs().get(req[3]));
        } else {
            books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
            storeBooks.add(book);
        }
        student.getBookBs().remove(req[3]);
        student.SetB(false);
    }

    public void dearReturn(String[] req) {
        Student student = students.get(req[1]);
        if (req[3].charAt(0) == 'B') {
            dealReturnB(req);
        } else {
            Book book = student.getBookCs().get(req[3]);
            if (student.getIsSmeared().get(req[3])) {
                System.out.println(req[0] + " " + req[1]
                        + " got punished by borrowing and returning librarian");
                System.out.println(req[0] + " " + "borrowing and returning librarian received "
                        + req[1] + "'s fine");
                System.out.println(req[0] + " " + req[1] + " returned " + book.getSchool() + "-"
                        + req[3] + " to self-service machine");
                System.out.println(req[0] + " self-service machine collected " +
                        book.getSchool() + "-" + req[3] + " from " + req[1]);
                System.out.println(req[0] + " " + book.getSchool() + "-" + req[3] +
                        " got repaired by logistics division in " + school);
                book.setSmeared(false);
            } else {
                System.out.println(req[0] + " " + req[1] + " returned " + book.getSchool() + "-"
                        + req[3] + " to self-service machine");
                System.out.println(req[0] + " self-service machine collected " +
                        book.getSchool() + "-" + req[3] + " from " + req[1]);
            }
            if (!student.getBookCs().get(req[3]).getSchool().equals(school)) {
                student.getBookCs().get(req[3]).setSrc(school);
                department.getReceiveBooks().add(student.getBookCs().get(req[3]));
            } else {
                books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
                storeBooks.add(book);
            }
            student.getBookCs().remove(req[3]);
        }
        student.getIsSmeared().remove(req[3]);
    }

    public void allocateBook(int date) {
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (order.isUseLess()) {
                orders.remove(i);
                i--;
                continue;
            }
            if (order.getDate() < date) {
                int pos = isNeed(order, date);
                if (pos != -1) {
                    System.out.println("[" + Day2Date(date) + "]" + " ordering librarian lent "
                            + school + "-" + order.getBookNumber() + " to " + order.getStudent());
                    System.out.println("[" + Day2Date(date) + "] " +
                            order.getStudent() + " borrowed " + school + "-" +
                            order.getBookNumber() + " from ordering librarian");
                    if (order.getBookNumber().charAt(0) == 'B') {
                        students.get(order.getStudent()).SetB(true);
                        students.get(order.getStudent()).getBookBs().
                                put(order.getBookNumber(), storeBooks.get(pos));
                        clearB(order);
                    } else {
                        students.get(order.getStudent()).getBookCs().
                                put(order.getBookNumber(), storeBooks.get(pos));
                    }
                    students.get(order.getStudent()).getIsSmeared().
                            put(order.getBookNumber(), false);
                    storeBooks.remove(pos);
                    orders.remove(i);
                    i--;
                }
            } else {
                break;
            }
        }
        Iterator<Book> iter = storeBooks.iterator();
        while (iter.hasNext()) {
            Book book = iter.next();
            if (book.getStoreDay() <= date) {
                books.put(book.getBookNumber(), book);
                if (bookCounter.containsKey(book.getBookNumber())) {
                    int tempCnt = bookCounter.get(book.getBookNumber());
                    bookCounter.put(book.getBookNumber(), tempCnt + 1);
                } else {
                    bookCounter.put(book.getBookNumber(), 1);
                }
                iter.remove();
            }
        }
    }

    public void clearB(Order order) {
        for (Order i : orders) {
            if (i.getStudent().equals(order.getStudent())
                    && i.getBookNumber().charAt(0) == 'B') {
                i.setUseLess(true);
                students.get(order.getStudent()).getOrderings().remove(order.getBookNumber());
            }
        }
    }

    public void clearB2(String student) {
        for (Order order : orders) {
            if (order.getStudent().equals(student) && order.getBookNumber().charAt(0) == 'B') {
                order.setUseLess(true);
                students.get(student).getOrderings().remove(order.getBookNumber());
            }
        }
    }

    public int isNeed(Order order, int yesterday) {
        for (Book book : storeBooks) {
            if (book.getBookNumber().equals(order.getBookNumber())) {
                if (order.getDate() < yesterday) {
                    return storeBooks.indexOf(book);
                }
            }
        }
        return -1;
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

    public HashMap<String, Book> getBooks() {
        return books;
    }

    public HashMap<String, Integer> getBookCounter() {
        return bookCounter;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public ArrayList<Order> getBuyBooks() {
        return buyBooks;
    }

    public HashMap<String, Boolean> getLentBooks() {
        return lentBooks;
    }

    public ArrayList<Order> getToOrderBooks() {
        return toOrderBooks;
    }

    public ArrayList<Book> getStoreBooks() {
        return storeBooks;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
