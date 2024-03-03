import java.math.BigInteger;

public class VaryPow {
    private BigInteger xup = BigInteger.ZERO;
    private BigInteger yup = BigInteger.ZERO;
    private BigInteger zup = BigInteger.ZERO;
    private BigInteger toTal = BigInteger.ZERO;
    private BigInteger coEffi = BigInteger.ZERO;

    public VaryPow(String in) {
        this.setCoEffi(new BigInteger(in));
    }

    public VaryPow() {
    }

    public void setAttri(BigInteger xup, BigInteger yup, BigInteger zup,
                         BigInteger coEffi, BigInteger toTal) {
        this.toTal = toTal;
        this.coEffi = coEffi;
        this.xup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : xup;
        this.yup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : yup;
        this.zup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : zup;
    }

    public Boolean judge(VaryPow otherVaryPow) {
        if (this.getXup().equals(otherVaryPow.getXup()) &&
                this.getYup().equals(otherVaryPow.getYup())
                && this.getZup().equals(otherVaryPow.getZup())) {
            return true;
        }
        return false;
    }

    public VaryPow add(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup, this.yup, this.zup,
                this.getCoEffi().add(otherVaryPow.getCoEffi()), this.getToTal());
        return varyPow;
    }

    public VaryPow sub(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup, this.yup, this.zup,
                this.getCoEffi().subtract(otherVaryPow.getCoEffi()), this.getToTal());
        return varyPow;
    }

    public VaryPow mul(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup.add(otherVaryPow.getXup()), this.yup.add(otherVaryPow.getYup()),
                this.zup.add(otherVaryPow.getZup()),
                this.getCoEffi().multiply(otherVaryPow.getCoEffi()), this.getToTal());
        return varyPow;
    }

    public BigInteger getCoEffi() {
        return coEffi;
    }

    public VaryPow setCoEffi(BigInteger coEffi) {
        this.coEffi = coEffi;
        return this;
    }

    public BigInteger getXup() {
        return xup;
    }

    public void setXup(BigInteger xup) {
        this.xup = xup;
    }

    public BigInteger getYup() {
        return yup;
    }

    public void setYup(BigInteger yup) {
        this.yup = yup;
    }

    public BigInteger getZup() {
        return zup;
    }

    public void setZup(BigInteger zup) {
        this.zup = zup;
    }

    public BigInteger getToTal() {
        return toTal;
    }

}
