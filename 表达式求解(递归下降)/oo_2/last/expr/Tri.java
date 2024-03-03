package expr;

public class Tri implements Factor {
    private Expr expr = new Expr();
    private String type;

    public void addExpr(Expr in) {
        this.expr = in;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expr.toString());
        sb.append(" " + type);

        return sb.toString();
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
