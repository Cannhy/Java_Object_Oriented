package expr;

import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private ArrayList<String> opes;
    private int position = 0;

    public Expr() {
        this.terms = new ArrayList<>();
        this.opes = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<String> getOpes() {
        return opes;
    }

    public void setOpes(ArrayList<String> opes) {
        this.opes = opes;
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            String operator1 = opes.get(position);
            position += 1;
            sb.append(" " + operator1);
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                String operator2 = opes.get(position);
                position += 1;
                sb.append(" " + operator2);
            }
        }

        position = 0;
        return sb.toString();
    }
}
