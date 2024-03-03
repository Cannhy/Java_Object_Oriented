import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage implements RedEnvelopeMessage {
    private int id;
    private int money;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.money = luckyMoney;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.group = null;
        this.socialValue = luckyMoney * 5;
    }

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Group messageGroup) {
        this.id = messageId;
        this.money = luckyMoney;
        this.person1 = messagePerson1;
        this.group = messageGroup;
        this.type = 1;
        this.person2 = null;
        this.socialValue = luckyMoney * 5;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getMoney() {
        return money;
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
