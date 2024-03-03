package expr;

import java.util.ArrayList;
import java.util.Iterator;

public class Diff implements Factor {
    private ArrayList<Expr> exprs = new ArrayList<>();
    private String type;

    public void addExpr(Expr in) {
        exprs.add(in);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Expr> iter = exprs.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next().toString());
            sb.append(" ");
        }
        sb.append(type);
        return sb.toString();
    }
}
