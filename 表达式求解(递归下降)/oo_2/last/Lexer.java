public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;
    private boolean isOpe;
    private boolean isNegator;

    public Lexer(String input) {
        isNegator = false;
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++ pos;
        }
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        if (isNegator) {
            pos++;
            isOpe = false;
            isNegator = false;
            curToken = "*";
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
            isOpe = true;
        }
        else if ("+-".indexOf(c) != -1) {
            if (isOpe) {
                curToken = String.valueOf(c);
                isOpe = false;
                pos += 1;
            }
            else {
                isOpe = true;
                isNegator = true;
                curToken = (c == '+') ? "1" : "-1";
            }
        }
        else if (c == '*' && input.charAt(pos + 1) == '*') {
            isOpe = false;
            pos += 2;
            curToken = "**";
        }
        else if (c == '*') {
            isOpe = false;
            pos += 1;
            curToken = "*";
        }
        else if (c == 'x' || c == 'y' || c == 'z') {
            pos += 1;
            curToken = String.valueOf(c);
            isOpe = true;
        }
        else if (c == '(' | c == ')') {
            pos += 1;
            curToken = String.valueOf(c);
            isOpe = (c != '(');
        }
        else if (c == 's' || c == 'c') {
            curToken = (c == 's') ? "sin" : "cos";
            pos += 4;
            isOpe = false;
        }
        //next1();
        else if (c == 'f' || c == 'g' || c == 'h') {
            curToken = (c == 'f') ? "f" : (c == 'g') ? "g" : "h";
            pos += 2;
            isOpe = false;
        }
        else if (c == ',') {
            curToken = ",";
            pos += 1;
            isOpe = false;
        }
    }

    public void next1() {
        char c = input.charAt(pos);
        if (c == 'f' || c == 'g' || c == 'h') {
            curToken = (c == 'f') ? "f" : (c == 'g') ? "g" : "h";
            pos += 2;
            isOpe = false;
        }
        else if (c == ',') {
            curToken = ",";
            pos += 1;
            isOpe = false;
        }
    }

    public String peek() {
        return  this.curToken;
    }
}
