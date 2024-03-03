import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VaryPow {
    private BigInteger xup = BigInteger.ZERO;
    private BigInteger yup = BigInteger.ZERO;
    private BigInteger zup = BigInteger.ZERO;

    private BigInteger coEffi = BigInteger.ZERO;

    private Boolean isUsed = false;

    private HashMap<String, BigInteger> triContent = new HashMap<>();

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public VaryPow(String in) {
        this.setCoEffi(new BigInteger(in));
    }

    public VaryPow() {
    }

    public void setAttri(BigInteger xup, BigInteger yup, BigInteger zup,
                         BigInteger coEffi, HashMap<String, BigInteger> triContent) {
        this.coEffi = coEffi;
        this.xup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : xup;
        this.yup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : yup;
        this.zup = (coEffi.equals(0)) ? BigInteger.valueOf(0) : zup;
        this.triContent = triContent;
    }

    public Boolean judge(VaryPow otherVaryPow) {
        if (this.getXup().equals(otherVaryPow.getXup())
                && this.getYup().equals(otherVaryPow.getYup())
                && this.getZup().equals(otherVaryPow.getZup())
                && this.triEqual(otherVaryPow)) {
            return true;
        }
        return false;
    }

    public Boolean triEqual(VaryPow otherVaryPow) {
        HashMap<String, BigInteger> hashMap = new HashMap<>();
        for (Map.Entry str : this.getTriContent().entrySet()) {
            hashMap.put((String) str.getKey(), (BigInteger) str.getValue());
        }
        HashMap<String, BigInteger> hashMap1 = new HashMap<>();
        for (Map.Entry str : otherVaryPow.getTriContent().entrySet()) {
            hashMap1.put((String) str.getKey(), (BigInteger) str.getValue());
        }
        Iterator iter1 = hashMap.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry temp1 = (Map.Entry) iter1.next();
            Iterator iter2 = hashMap1.entrySet().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                Map.Entry temp2 = (Map.Entry) iter2.next();
                if (temp1.getKey().equals(temp2.getKey())
                        && temp1.getValue().equals(temp2.getValue())) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        if ((!hashMap.isEmpty()) || (!hashMap1.isEmpty())) {
            return false;
        }
        return true;
    }

    public VaryPow add(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup, this.yup, this.zup,
                this.getCoEffi().add(otherVaryPow.getCoEffi()),
                this.getTriContent());
        return varyPow;
    }

    public VaryPow sub(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup, this.yup, this.zup,
                this.getCoEffi().subtract(otherVaryPow.getCoEffi()),
                this.getTriContent());
        return varyPow;
    }

    public VaryPow mul(VaryPow otherVaryPow) {
        HashMap<String, BigInteger> thi = new HashMap<>();
        for (Map.Entry str : this.getTriContent().entrySet()) {
            thi.put((String) str.getKey(), (BigInteger) str.getValue());
        }
        HashMap<String, BigInteger> othe = new HashMap<>();
        for (Map.Entry str : otherVaryPow.getTriContent().entrySet()) {
            othe.put((String) str.getKey(), (BigInteger) str.getValue());
        }
        HashMap<String, BigInteger> temp = new HashMap<>();
        Iterator iter1 = thi.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry temp1 = (Map.Entry) iter1.next();
            Iterator iter2 = othe.entrySet().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                Map.Entry temp2 = (Map.Entry) iter2.next();
                if (temp1.getKey().equals(temp2.getKey())) {
                    //System.out.println("in");
                    used = true;
                    temp.put((String) temp1.getKey(), ((BigInteger) temp1.getValue()).
                            add((BigInteger) temp2.getValue()));
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        for (Map.Entry<String, BigInteger> str : thi.entrySet()) {
            temp.put(str.getKey(), str.getValue());
        }
        for (Map.Entry<String, BigInteger> str : othe.entrySet()) {
            temp.put(str.getKey(), str.getValue());
        }
        //System.out.println(temp);
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(this.xup.add(otherVaryPow.getXup()), this.yup.add(otherVaryPow.getYup()),
                this.zup.add(otherVaryPow.getZup()),
                this.getCoEffi().multiply(otherVaryPow.getCoEffi()),
                temp);
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

    public HashMap<String, BigInteger> getTriContent() {
        return triContent;
    }

    public void setTriContent(HashMap<String, BigInteger> triContent) {
        this.triContent = triContent;
    }
}
