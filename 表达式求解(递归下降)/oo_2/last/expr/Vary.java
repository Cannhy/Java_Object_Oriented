package expr;

public class Vary implements Factor {
    // 变量因子
    private final String vary;

    public Vary(String vary) {
        this.vary = vary;
    }

    public String toString() {
        return this.vary;
    }
}
