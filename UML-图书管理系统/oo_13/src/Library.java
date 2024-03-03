import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Library {
    private HashMap<String, Book> books;
    private HashMap<String, Integer> bookCounter;
    private HashMap<String, Student> students;
    private ArrayList<Order> orders;
    private ArrayList<Book> storeBooks;
    private int yesterday;
    private int[] enumDay;

    public Library() {
        this.books = new HashMap<>();
        this.bookCounter = new HashMap<>();
        this.students = new HashMap<>();
        this.orders = new ArrayList<>();
        this.storeBooks = new ArrayList<>();
        this.yesterday = 1;
        enumDay = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    public void addBooks(Scanner scanner, int n) {
        for (int i = 0; i < n; i++) {
            String temp = scanner.nextLine();
            //System.out.println(temp);
            String[] temp2 = temp.split(" ");
            //System.out.println(temp2[0]);
            Book book = new Book(temp2[0]);
            books.put(temp2[0], book);
            bookCounter.put(temp2[0], Integer.parseInt(temp2[1]));
        }
    }

    public void run(Scanner scanner, int m) {
        for (int i = 0; i < m; i++) {
            String req = scanner.nextLine();
            String[] reqSplit = req.split(" ");
            arrangeBook(reqSplit);
            switch (reqSplit[2]) {
                case "borrowed":
                    dealBorrowed(reqSplit);
                    break;
                case "smeared":
                    dealSmeared(reqSplit);
                    break;
                case "lost":
                    dealLost(reqSplit);
                    break;
                case "returned":
                    dearReturn(reqSplit);
                    break;
                default:
                    break;
            }
        }
    }

    public void dealBorrowed(String[] req) {
        System.out.println(req[0] + " " + req[1] + " " +
                "queried " + req[3] + " from self-service machine");
        if (req[3].charAt(0) == 'A') {
            return;
        }
        if (!students.containsKey(req[1])) {
            Student student = new Student(req[1]);
            students.put(req[1], student);
        }
        int tempCnt = bookCounter.get(req[3]);
        if (req[3].charAt(0) == 'B') {
            if (tempCnt != 0) {
                bookCounter.put(req[3], tempCnt - 1);
                if (!students.get(req[1]).HaveB()) {
                    students.get(req[1]).getBookBs().put(req[3], books.get(req[3]));
                    students.get(req[1]).SetB(true);
                    students.get(req[1]).getIsSmeared().put(req[3], false);
                    clearB2(req[1]);
                    System.out.println(req[0] + " " + req[1] + " borrowed " + req[3]
                            + " from borrowing and returning librarian");
                } else {
                    books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
                    storeBooks.add(books.get(req[3]));
                }
            } else {
                addOrderB(req);
            }
        } else if (req[3].charAt(0) == 'C') {
            if (tempCnt != 0) {
                bookCounter.put(req[3], tempCnt - 1);
                if (!students.get(req[1]).IsHaveBook(req[3])) {
                    students.get(req[1]).getBookCs().put(req[3], books.get(req[3]));
                    students.get(req[1]).getIsSmeared().put(req[3], false);
                    System.out.println(req[0] + " " + req[1] + " borrowed " + req[3]
                            + " from self-service machine");
                } else {
                    books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
                    storeBooks.add(books.get(req[3]));
                }
            } else {
                addOrderC(req);
            }
        }
    }

    public void addOrderB(String[] req) {
        Student student = students.get(req[1]);
        String date = req[0].substring(1, 11);
        if (!student.HaveB()) {
            if (!student.getOrders().containsKey(date)) {
                if (!student.getOrderings().contains(req[3])) {
                    HashMap<String, Book> hashMap = new HashMap<>();
                    student.getOrders().put(date, hashMap);
                    student.getOrderings().add(req[3]);
                    hashMap.put(req[3], books.get(req[3]));
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                    System.out.println(req[0] + " " + req[1] + " ordered " + req[3]
                            + " from ordering librarian");
                }
            } else {
                if (student.getOrders().get(date).size() < 3 &&
                        !student.getOrderings().contains(req[3])) {
                    student.getOrders().get(date).put(req[3], books.get(req[3]));
                    student.getOrderings().add(req[3]);
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                    System.out.println(req[0] + " " + req[1] + " ordered " + req[3]
                            + " from ordering librarian");
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
                    HashMap<String, Book> hashMap = new HashMap<>();
                    student.getOrders().put(date, hashMap);
                    student.getOrderings().add(req[3]);
                    hashMap.put(req[3], books.get(req[3]));
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                    System.out.println(req[0] + " " + req[1] + " ordered " + req[3]
                            + " from ordering librarian");
                }
            } else {
                if (student.getOrders().get(date).size() < 3 &&
                        !student.getOrderings().contains(req[3])) {
                    student.getOrders().get(date).put(req[3], books.get(req[3]));
                    student.getOrderings().add(req[3]);
                    Order order = new Order(req, Date2Day(req[0].substring(1, 11)));
                    orders.add(order);
                    System.out.println(req[0] + " " + req[1] + " ordered " + req[3]
                            + " from ordering librarian");
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
        Student student = students.get(req[1]);
        if (req[3].charAt(0) == 'B') {
            student.getBookBs().remove(req[3]);
            student.SetB(false);
        } else {
            student.getBookCs().remove(req[3]);
        }
        student.getIsSmeared().remove(req[3]);
    }

    public void dearReturn(String[] req) {
        Student student = students.get(req[1]);

        if (req[3].charAt(0) == 'B') {
            Book book = student.getBookBs().get(req[3]);
            if (student.getIsSmeared().get(req[3])) {
                System.out.println(req[0] + " " + req[1]
                        + " got punished by borrowing and returning librarian");
                System.out.println(req[0] + " " + req[1] + " returned " + req[3]
                        + " to borrowing and returning librarian");
                System.out.println(req[0] + " " + req[3] + " got repaired by logistics division");
                book.setSmeared(false);
            } else {
                System.out.println(req[0] + " " + req[1] + " returned " + req[3]
                        + " to borrowing and returning librarian");
            }
            books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
            storeBooks.add(book);
            student.getBookBs().remove(req[3]);
            student.SetB(false);
        } else {
            Book book = student.getBookCs().get(req[3]);
            if (student.getIsSmeared().get(req[3])) {
                System.out.println(req[0] + " " + req[1]
                        + " got punished by borrowing and returning librarian");
                System.out.println(req[0] + " " + req[1] + " returned " + req[3]
                        + " to self-service machine");
                System.out.println(req[0] + " " + req[3] + " got repaired by logistics division");
                book.setSmeared(false);
            } else {
                System.out.println(req[0] + " " + req[1] + " returned " + req[3]
                        + " to self-service machine");
            }
            books.get(req[3]).setStoreDay(Date2Day(req[0].substring(1, 11)));
            storeBooks.add(book);
            student.getBookCs().remove(req[3]);
        }
        student.getIsSmeared().remove(req[3]);
    }

    public void arrangeBook(String[] req) {
        int today = Date2Day(req[0].substring(1, 11));
        while (yesterday <= today) {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (order.isUseLess()) {
                    orders.remove(i);
                    i--;
                    continue;
                }
                if (order.getDate() < yesterday) {
                    int pos = isNeed(order, yesterday);
                    //System.out.println(pos);
                    if (pos != -1) {
                        System.out.println("[" + Day2Date(yesterday) + "] " +
                                order.getStudent() + " borrowed " +
                                order.getBookNumber() + " from ordering librarian");
                        students.get(order.getStudent()).getOrderings().remove(req[3]);
                        if (order.getBookNumber().charAt(0) == 'B') {
                            students.get(order.getStudent()).SetB(true);
                            students.get(order.getStudent()).getBookBs().
                                    put(order.getBookNumber(), books.get(order.getBookNumber()));
                            clearB(order);
                        } else {
                            students.get(order.getStudent()).getBookCs().
                                    put(order.getBookNumber(), books.get(order.getBookNumber()));
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
                if (book.getStoreDay() < yesterday) {
                    books.put(book.getBookNumber(), book);
                    int tempCnt = bookCounter.get(book.getBookNumber());
                    bookCounter.put(book.getBookNumber(), tempCnt + 1);
                    iter.remove();
                }
            }
            yesterday += 3;
        }
    }

    public void clearB(Order order) {
        for (Order i : orders) {
            if (i.getStudent().equals(order.getStudent())
                    && books.get(i.getBookNumber()).isBtype()) {
                i.setUseLess(true);
                students.get(order.getStudent()).getOrderings().remove(order.getBookNumber());
            }
        }
    }

    public void clearB2(String student) {
        for (Order order : orders) {
            if (order.getStudent().equals(student) && books.get(order.getBookNumber()).isBtype()) {
                order.setUseLess(true);
                students.get(student).getOrderings().remove(order.getBookNumber());
            }
        }
    }

    public int isNeed(Order order, int yesterday) {
        for (Book book : storeBooks) {
            if (book.getBookNumber().equals(order.getBookNumber())) {
                if (book.getStoreDay() < yesterday) {
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
}
