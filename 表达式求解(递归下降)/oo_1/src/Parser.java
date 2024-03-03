import expr.Number;
import expr.Expr;
import expr.Term;
import expr.Vary;
import expr.Power;
import expr.Factor;

import java.math.BigInteger;

public class Parser extends Simplify {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
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
            else {
                BigInteger num = BigInteger.valueOf(Long.parseLong(lexer.peek()));
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
