import expr.Expr;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SdFunc sdFunc = new SdFunc();
        String n = scanner.nextLine().trim();
        //scanner.next();
        for (int i = 0; i < Integer.parseInt(n); i += 1) {
            String f = trimInput(spaceInput(scanner.nextLine()));
            //System.out.println(f);
            sdFunc.addFun(f);
            //System.out.println(sdFunc.getFuncTion());
        }
        String input = scanner.nextLine();
        //String afterPro = sdFunc.replac(trimInput(spaceInput(input)));
        Lexer lexer = new Lexer(trimInput(spaceInput(input)));
        Parser parser = new Parser(lexer, sdFunc);

        Expr expr = parser.parseExpr();
        //System.out.println(expr.toString());
        Simplify simplify = new Simplify();
        simplify.setExps(expr);
        simplify.setSdFunc(sdFunc);
        String newInput = simplify.simple2();
        //System.out.println(newInput);
        Lexer newLexer = new Lexer(trimInput(spaceInput(newInput)));
        Parser newParser = new Parser(newLexer, sdFunc);
        Expr expr1 = newParser.parseExpr();
        Simplify simplify1 = new Simplify();
        simplify1.setExps(expr1);
        simplify1.setSdFunc(sdFunc);
        //System.out.println(expr1.toString());
        Poly poly = simplify1.simPle();
        /*for (VaryPow te1 : poly.getExp()){
            System.out.println(te1.getTriContent());
            System.out.println(te1.getCoEffi());
        }*/
        System.out.println(poly.toString());
    }

    // delete space and t
    public static String spaceInput(String input) {
        StringBuilder sb = new StringBuilder();
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