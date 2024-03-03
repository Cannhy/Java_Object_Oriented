import expr.Expr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SdFunc  {
    private HashMap<String, ArrayList<String>> funcTion = new HashMap<>();
    //private HashMap<String, Integer> Number = new HashMap<>();
    private int num;

    public Integer getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = Integer.parseInt(num);
    }

    public SdFunc() {
    }

    public void addFun(String input, int order) throws IOException, ClassNotFoundException {
        ArrayList<String> arrayList = new ArrayList<>();
        String para = input.split("=")[0];
        String cont = input.split("=")[1];
        for (int i = para.indexOf('(') + 1; i < para.length() - 1; i++) {
            if (para.charAt(i) != ',') {
                arrayList.add(para.substring(i, i + 1));
            }
        }
        arrayList.add(preProcess(cont, order));
        this.funcTion.put(input.substring(0, 1), arrayList);
    }

    public String replac(String input, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String[] temp = input.split(" ");
        ArrayList<String> fun = this.funcTion.get(type);
        Boolean usd = false;
        for (int i = 0; i < fun.get(fun.size() - 1).length(); i++) {
            usd = false;
            for (int j = 0; j < fun.size() - 1; j++) {
                if (fun.get(j).equals(fun.get(fun.size() - 1).substring(i, i + 1))) {
                    usd = true;
                    sb.append("(").append(temp[j]).append(")");
                    break;
                }
            }
            if (!usd) {
                sb.append(fun.get(fun.size() - 1).charAt(i));
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String preProcess(String string, int order) throws IOException, ClassNotFoundException {
        if (order == 0) {
            if (string.indexOf("d") == -1) {
                return string;
            } else {
                Lexer lexer = new Lexer(string);
                Parser parser = new Parser(lexer, this);
                Expr expr = parser.parseExpr();
                Simplify simplify = new Simplify();
                simplify.setExps(expr);
                simplify.setSdFunc(this);
                return simplify.simPle().toString();
            }
        }
        Boolean judge = false;
        if (order == 1 || order == 2) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry str : this.funcTion.entrySet()) {
                if (string.indexOf((String) str.getKey()) != -1) {
                    judge = true;
                    Lexer lexer = new Lexer(string);
                    Parser parser = new Parser(lexer, this);
                    Expr expr = parser.parseExpr();
                    Simplify simplify = new Simplify();
                    simplify.setExps(expr);
                    simplify.setSdFunc(this);
                    stringBuilder.append(trimInput(spaceInput(simplify.simple2())));
                    break;
                }
            }
            if (!judge) {
                stringBuilder.append(string);
            }
            if (!stringBuilder.toString().contains("d")) {
                return stringBuilder.toString();
            } else {
                Lexer lexer = new Lexer(stringBuilder.toString());
                Parser parser = new Parser(lexer, this);
                Expr expr = parser.parseExpr();
                Simplify simplify = new Simplify();
                simplify.setExps(expr);
                simplify.setSdFunc(this);
                return simplify.simPle().toString();
            }
        }
        return string;
    }

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

    public HashMap<String, ArrayList<String>> getFuncTion() {
        return funcTion;
    }

    public void setFuncTion(HashMap<String, ArrayList<String>> funcTion) {
        this.funcTion = funcTion;
    }
}
