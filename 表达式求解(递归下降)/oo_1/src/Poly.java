import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Poly {
    private ArrayList<VaryPow> exp;
    private BigInteger maxId;

    public Poly(String in) {
        this.exp = new ArrayList<>();
        //ArrayList arrayList = new ArrayList();
        if (in.equals("x") || in.equals("y") || in.equals("z")) {
            VaryPow varyPow = new VaryPow();
            if (in.equals("x")) {
                varyPow.setXup(BigInteger.valueOf(1));
                varyPow.setCoEffi(BigInteger.valueOf(1));
            }
            if (in.equals("y")) {
                varyPow.setYup(BigInteger.valueOf(1));
                varyPow.setCoEffi(BigInteger.valueOf(1));
            }
            if (in.equals("z")) {
                varyPow.setZup(BigInteger.valueOf(1));
                varyPow.setCoEffi(BigInteger.valueOf(1));
            }
            exp.add(varyPow);
            //exp.put(BigInteger.valueOf(1), arrayList);
            maxId = new BigInteger("1");
        }
        else if (in.equals("")) {
            maxId = new BigInteger("0");
        }
        else {
            VaryPow varyPow = new VaryPow(in);
            exp.add(varyPow);
            //exp.put(new BigInteger("0"), arrayList);
            maxId = new BigInteger("0");
        }
    }

    public BigInteger getMaxId() {
        return maxId;
    }

    public ArrayList<VaryPow> getExp() {
        return exp;
    }

    public void setExp(ArrayList<VaryPow> exp) {
        this.exp = exp;
    }

    public void setMaxId(BigInteger maxId) {
        this.maxId = maxId;
    }

    public Poly getAdd(Poly otherPoly) {
        /*BigInteger maxId = (this.maxId.compareTo(otherPoly.getMaxId()) > 0) ?
                this.maxId : otherPoly.getMaxId();*/
        //poly.setMaxId(maxId);
        //BigInteger count = new BigInteger("0");
        Iterator<VaryPow> iter1 = this.getExp().iterator();
        Poly polyTemp = new Poly("");
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.getExp().iterator();
            Boolean isUsed = false;
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                if (temp1.judge(temp2)) {
                    polyTemp.getExp().add(temp1.add(temp2));
                    iter2.remove();
                    isUsed = true;
                    break;
                }
                /*else if (temp1.isZero() && iter1.hasNext()) {
                    polyTemp.getExp().add(temp2);
                    iter2.remove();
                }
                else if (temp2.isZero()) {
                    iter2.remove();
                }*/
                /*else {
                    polyTemp.getExp().add(temp2);
                    iter2.remove();
                }*/
            }
            if (isUsed) {
                iter1.remove();
            }
        }
        for (VaryPow temp : this.getExp()) {
            polyTemp.getExp().add(temp);
        }
        for (VaryPow temp : otherPoly.getExp()) {
            polyTemp.getExp().add(temp);
        }
        return polyTemp;
    }

    public Poly getSub(Poly otherPoly) {
        /*BigInteger maxId = (this.maxId.compareTo(otherPoly.getMaxId()) > 0) ?
                this.maxId : otherPoly.getMaxId();*/
        Poly poly = new Poly("");
        //poly.setMaxId(maxId);
        BigInteger count = new BigInteger("0");
        Iterator<VaryPow> iter1 = this.exp.iterator();
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.exp.iterator();
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                if (temp1.judge(temp2)) {
                    poly.getExp().add(temp1.sub(temp2));
                }
                else {
                    poly.getExp().add(temp1);
                    poly.getExp().add(temp2.setCoEffi(temp2.getCoEffi().
                            multiply(new BigInteger("-1"))));
                }
            }
        }
        return poly;
    }

    public Poly getMul(Poly otherPoly) {
        Poly poly = new Poly("0");
        //BigInteger count = new BigInteger("0");
        //poly.setMaxId(this.getMaxId().add(otherPoly.getMaxId()));
        Iterator<VaryPow> iter1 = this.getExp().iterator();
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.getExp().iterator();
            while (iter2.hasNext()) {
                Poly temp = new Poly("");
                VaryPow temp2 = iter2.next();
                temp.getExp().add(temp1.mul(temp2));
                poly = poly.getAdd(temp);
            }
        }
        return poly;
    }

    public Poly power(Poly otherPoly) {
        Poly poly = new Poly("1");
        BigInteger count = new BigInteger("0");
        while (count.compareTo(otherPoly.getExp().get(0).getCoEffi()) < 0) {
            poly = poly.getMul(this);
            count = count.add(BigInteger.valueOf(1));
        }
        return poly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (exp.size() == 1 && exp.get(0).getCoEffi().equals(BigInteger.ZERO)) {
            return "0";
        }
        else {
            Iterator<VaryPow> iter = exp.iterator();
            while (iter.hasNext()) {
                VaryPow varyPow = iter.next();
                if (varyPow.getCoEffi().equals(BigInteger.ZERO)) {
                    continue;
                }
                else {
                    sb.append(prInt(varyPow));
                    sb.append(print2(varyPow));
                }
            }
        }
        if (sb.length() > 0 && sb.toString().charAt(0) == '+') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public String prInt(VaryPow varyPow) {
        StringBuilder sb = new StringBuilder();
        BigInteger a = varyPow.getCoEffi();
        BigInteger x = varyPow.getXup();
        BigInteger y = varyPow.getYup();
        BigInteger z = varyPow.getZup();
        if (a.compareTo(BigInteger.valueOf(0)) < 0) {
            if (x.equals(BigInteger.valueOf(0)) && y.equals(BigInteger.valueOf(0))
                    && z.equals(BigInteger.valueOf(0))) {
                sb.append(a);
            }
            else {
                if (a.equals(-1)) {
                    sb.append("-");
                }
                else {
                    sb.append(a).append("*");
                }
            }
        }
        else {
            if (x.equals(BigInteger.valueOf(0)) && y.equals(BigInteger.valueOf(0))
                    && z.equals(BigInteger.valueOf(0))) {
                sb.append("+").append(a);
            } else {
                if (a.equals(BigInteger.valueOf(1))) {
                    sb.append("+");
                } else {
                    sb.append("+").append(a).append("*");
                }
            }
        }
        return sb.toString();
    }

    public String print2(VaryPow varyPow) {
        StringBuilder sb = new StringBuilder();
        BigInteger x = varyPow.getXup();
        BigInteger y = varyPow.getYup();
        BigInteger z = varyPow.getZup();
        if (!x.equals(BigInteger.ZERO) && !y.equals(BigInteger.ZERO)
                && !z.equals(BigInteger.ZERO)) {
            sb.append("x**").append(x).append("*").append("y**").append(y).
                    append("*").append("z**").append(z);
        }
        if (x.equals(BigInteger.ZERO) && !y.equals(BigInteger.ZERO)
                && !z.equals(BigInteger.ZERO)) {
            sb.append("y**").append(y).append("*").append("z**").append(z);
        }
        if (!x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)
                && !z.equals(BigInteger.ZERO)) {
            sb.append("x**").append(x).append("*").append("z**").append(z);
        }
        if (!x.equals(BigInteger.ZERO) && !y.equals(BigInteger.ZERO)
                && z.equals(BigInteger.ZERO)) {
            sb.append("x**").append(x).append("*").append("y**").append(y);
        }
        if (x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)
                && !z.equals(BigInteger.ZERO)) {
            if (z.equals(BigInteger.ONE)) {
                sb.append("z");
            }
            else if (x.equals(BigInteger.valueOf(2))) {
                sb.append("z*z");
            }
            else {
                sb.append("z**").append(z);
            }
        }
        if (x.equals(BigInteger.ZERO) && !y.equals(BigInteger.ZERO)
                && z.equals(BigInteger.ZERO)) {
            if (y.equals(BigInteger.ONE)) {
                sb.append("y");
            }
            else if (y.equals(BigInteger.valueOf(2))) {
                sb.append("y*y");
            }
            else {
                sb.append("y**").append(y);
            }
        }
        if (!x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)
                && z.equals(BigInteger.ZERO)) {
            if (x.equals(BigInteger.ONE)) {
                sb.append("x");
            }
            else if (x.equals(BigInteger.valueOf(2))) {
                sb.append("x*x");
            }
            else {
                sb.append("x**").append(x);
            }
        }
        return sb.toString();
    }
}
