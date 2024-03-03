import expr.Factor;
import expr.Expr;
import expr.Tri;
import expr.Vary;
import expr.Power;
import expr.Number;
import expr.Diff;
import expr.SelFunc;
import expr.Term;

import java.math.BigInteger;

public class Parser extends Simplify {
    private final Lexer lexer;
    private SdFunc sdFunc;

    public Parser(Lexer lexer, SdFunc sdFunc1) {
        this.lexer = lexer;
        this.sdFunc = sdFunc1;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.getOpes().add(lexer.peek());
            lexer.next();
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parsePower());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parsePower());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            return expr;
        }
        else {
            if (lexer.peek().equals("x") || lexer.peek().equals("y") || lexer.peek().equals("z")) {
                String vary = lexer.peek();
                lexer.next();
                Vary vari = new Vary(vary);
                return vari;
            }
            else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
                Tri tri = new Tri();
                tri.setType(lexer.peek());
                lexer.next();
                tri.addExpr(parseExpr());
                lexer.next();
                return tri;
            }

            else if (lexer.peek().equals("f") || lexer.peek().equals("g")
                    || lexer.peek().equals("h")) {
                SelFunc selFunc = new SelFunc();
                selFunc.setType(lexer.peek());
                String temp = lexer.peek();
                for (int i = 0; i < sdFunc.getFuncTion().get(temp).size() - 1; i++) {
                    lexer.next();
                    selFunc.addExpr(parseExpr());
                }
                lexer.next();
                return selFunc;
            }
            else if (lexer.peek().indexOf('d') != -1) {
                Diff diff = new Diff();
                if (lexer.peek().indexOf('x') != -1) {
                    diff.setType("dx");
                } else if (lexer.peek().indexOf('y') != -1) {
                    diff.setType("dy");
                } else {
                    diff.setType("dz");
                }
                lexer.next();
                diff.addExpr(parseExpr());
                lexer.next();
                return diff;
            }
            else {
                BigInteger num = new BigInteger(lexer.peek());
                lexer.next();
                Number number = new Number(num);
                return number;
            }
        }
    }

    public Power parsePower() {
        Power power = new Power();
        power.addFactor(parseFactor());
        while (lexer.peek().equals("**")) {
            lexer.next();
            power.addFactor(parseFactor());
        }
        return power;
    }
}
