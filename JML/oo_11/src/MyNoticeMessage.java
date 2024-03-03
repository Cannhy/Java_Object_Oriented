import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage implements NoticeMessage {
    private int id;
    private String string;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.string = noticeString;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.type = 0;
        this.group = null;
        this.socialValue = string.length();
    }

    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.string = noticeString;
        this.socialValue = string.length();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }
}
