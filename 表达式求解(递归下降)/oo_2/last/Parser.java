import expr.Expr;
import expr.Tri;
import expr.Number;
import expr.SelFunc;
import expr.Factor;
import expr.Power;
import expr.Vary;
import expr.Term;

import java.math.BigInteger;

public class Parser {
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
            else if (lexer.peek().equals("sin")) {
                Tri tri = new Tri();
                tri.setType("sin");
                lexer.next();
                tri.addExpr(parseExpr());
                lexer.next();
                return tri;
            }
            else if (lexer.peek().equals("cos")) {
                Tri tri = new Tri();
                tri.setType("cos");
                lexer.next();
                tri.addExpr(parseExpr());
                lexer.next();
                return tri;
            }
            else if (lexer.peek().equals("f")) {
                SelFunc selFunc = new SelFunc();
                selFunc.setType("f");
                for (int i = 0; i < sdFunc.getFuncTion().get("f").size() - 1; i++) {
                    lexer.next();
                    selFunc.addExpr(parseExpr());
                }
                lexer.next();
                return selFunc;
            }
            else if (lexer.peek().equals("g")) {
                SelFunc selFunc = new SelFunc();
                selFunc.setType("g");
                for (int i = 0; i < sdFunc.getFuncTion().get("g").size() - 1; i++) {
                    lexer.next();
                    selFunc.addExpr(parseExpr());
                }
                lexer.next();
                return selFunc;
            }
            else if (lexer.peek().equals("h")) {
                SelFunc selFunc = new SelFunc();
                selFunc.setType("h");
                for (int i = 0; i < sdFunc.getFuncTion().get("h").size() - 1; i++) {
                    lexer.next();
                    selFunc.addExpr(parseExpr());
                }
                lexer.next();
                return selFunc;
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
