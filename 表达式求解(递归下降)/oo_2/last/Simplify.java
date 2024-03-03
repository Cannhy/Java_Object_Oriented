import expr.Expr;

import java.util.ArrayList;

public class Simplify {
    private String [] exps;
    private ArrayList<Poly> polys = new ArrayList<>();
    private boolean judge;

    private SdFunc sdFunc = new SdFunc();

    public String[] getExps() {
        return exps;
    }

    public void setExps(Expr exps) {
        this.exps = exps.toString().split(" ");
    }

    public String simple2() {
        ArrayList<String> temps = new ArrayList<>();
        for (String exp : exps) {
            if (exp.equals("f") || exp.equals("g") || exp.equals("h")) {
                StringBuilder sb = new StringBuilder();
                for (int i = sdFunc.getFuncTion().get(exp).size() - 1; i > 0; i--) {
                    sb.append(temps.get(temps.size() - i)).append(" ");
                    //b.append(" ");
                    temps.remove(temps.size() - i);
                }
                temps.add(sdFunc.replac(sb.toString(), exp));
            }
            /*else if (exp.equals("g")) {
                StringBuilder sb = new StringBuilder();
                for (int i = sdFunc.getFuncTion().get("g").size() - 1; i > 0; i--) {
                    sb.append(temps.get(temps.size() - i)).append(" ");
                    //sb.append(" ");
                    temps.remove(temps.size() - i);
                }
                temps.add(sdFunc.replac(sb.toString(), "g"));
            }
            else if (exp.equals("h")) {
                StringBuilder sb = new StringBuilder();
                for (int i = sdFunc.getFuncTion().get("h").size() - 1; i > 0; i--) {
                    sb.append(temps.get(temps.size() - i)).append(" ");
                    //sb.append(" ");
                    temps.remove(temps.size() - i);
                }
                temps.add(sdFunc.replac(sb.toString(), "h"));
            }*/
            else if (exp.equals("+")) {
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(temps.get(temps.size() - 2)).append("+")
                        .append(temps.get(temps.size() - 1)).append(")");
                temps.remove(temps.size() - 1);
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            }
            else if (exp.equals("-")) {
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(temps.get(temps.size() - 2)).append("-")
                        .append(temps.get(temps.size() - 1)).append(")");
                temps.remove(temps.size() - 1);
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            } else if (exp.equals("*")) {
                StringBuilder sb = new StringBuilder();
                sb.append(temps.get(temps.size() - 2)).append("*")
                        .append(temps.get(temps.size() - 1));
                temps.remove(temps.size() - 1);
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            }
            else if (exp.equals("**")) {
                StringBuilder sb = new StringBuilder();
                sb.append(temps.get(temps.size() - 2)).append("**")
                        .append(temps.get(temps.size() - 1));
                temps.remove(temps.size() - 1);
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            }
            else if (exp.equals("sin") || exp.equals("cos")) {
                StringBuilder sb = new StringBuilder();
                /*if (exp.equals("sin")) {
                    sb.append("sin(");
                }
                else {
                    sb.append("cos(");
                }*/
                sb.append(exp).append("(");
                sb.append(temps.get(temps.size() - 1)).append(")");
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            }
            /*else if (exp.equals("cos")) {
                StringBuilder sb = new StringBuilder();
                sb.append("cos(").append(temps.get(temps.size() - 1)).append(")");
                temps.remove(temps.size() - 1);
                temps.add(sb.toString());
            }*/
            else {
                temps.add(exp);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : temps) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
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
                /*for (VaryPow varyPow : poly2.getExp()) {
                    System.out.println(varyPow.getTriContent());
                }*/
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
                /*for (VaryPow varyPow : poly2.getExp()) {
                    System.out.println(varyPow.getTriContent());
                }*/
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
            else if (exp.equals("sin")) {
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly = poly1.sin(poly1);
                polys.remove(poly1);
                polys.add(poly);
            }
            else if (exp.equals("cos")) {
                Poly poly1 = polys.get(polys.size() - 1);
                Poly poly = poly1.cos(poly1);
                polys.remove(poly1);
                polys.add(poly);
            }
            else {
                Poly poly = new Poly(exp);
                polys.add(poly);
            }
        }
        return polys.get(0);
    }

    public void setSdFunc(SdFunc sdFunc) {
        this.sdFunc = sdFunc;
    }
}
