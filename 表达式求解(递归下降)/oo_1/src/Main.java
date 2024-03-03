import expr.Expr;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(trimInput(spaceInput(input)));
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        //System.out.println(expr.toString());
        Simplify simplify = new Simplify();
        simplify.setExps(expr);
        Poly poly = simplify.simPle();
        System.out.println(poly.toString());
        /*for (VaryPow te1 : poly.getExp()){
            System.out.println(te1.getXup());
            System.out.println(te1.getCoEffi());
        }*/
    }

    // delete space and t
    public static String spaceInput(String input) {
        StringBuilder sb = new StringBuilder();
        //not bu zero !!!!!!!!!!!!!!!!!!!!!!
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ' || input.charAt(i) == '\t') {
                continue;
            }
            else {
                sb.append(input.charAt(i));
            }
        }
        return sb.toString();
    }

    // delete extra + -
    public static String trimInput(String input) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (input.indexOf('+') == 0 || input.indexOf('-') == 0) {
            sb.append("0");
        }
        while (i < input.length()) {
            boolean flag = true;
            if (input.charAt(i) == '*' && input.charAt(i + 1) == '*'
                    && input.charAt(i + 2) == '+') {
                sb.append("**");
                i = i + 3;
                continue;
            }
            if (input.charAt(i) == '+' | input.charAt(i) == '-') {
                while (i < input.length() && (input.charAt(i) == '+' | input.charAt(i) == '-')) {
                    flag = (input.charAt(i) == '-') ? !flag : flag;
                    i++;
                }
                if (flag) {
                    sb.append('+');
                }
                else {
                    sb.append('-');
                }
            }
            else {
                sb.append(input.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
}