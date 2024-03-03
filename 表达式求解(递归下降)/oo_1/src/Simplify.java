import expr.Expr;

import java.util.ArrayList;

public class Simplify extends Poly {
    private String [] exps;
    private ArrayList<Poly> polys = new ArrayList<>();
    private boolean judge;

    public String[] getExps() {
        return exps;
    }

    public void setExps(Expr exps) {
        this.exps = exps.toString().split(" ");
    }

    public Poly simPle() {
        for (String exp : exps) {
            if (!judge) {
                polys.add(new Poly(exp));
                judge = true;
                continue;
            }
            if (exp.equals("+")) {
                Poly poly = polys.get(polys.size() - 2);
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly2 = poly.getAdd(poly1);
                polys.remove(poly);
                polys.remove(poly1);
                polys.add(poly2);
            }
            else if (exp.equals("*")) {
                Poly poly = polys.get(polys.size() - 2);
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly2 = poly.getMul(poly1);
                polys.remove(poly);
                polys.remove(poly1);
                polys.add(poly2);
            }
            else if (exp.equals("**")) {
                Poly poly = polys.get(polys.size() - 2);
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly2 = poly.power(poly1);
                polys.remove(poly);
                polys.remove(poly1);
                polys.add(poly2);
            }
            else if (exp.equals("-")) {
                Poly poly = polys.get(polys.size() - 2);
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly2 = poly.getSub(poly1);
                polys.remove(poly);
                polys.remove(poly1);
                polys.add(poly2);
            }
            else {
                Poly poly = new Poly(exp);
                polys.add(poly);
            }
        }
        return polys.get(0);
    }
}
