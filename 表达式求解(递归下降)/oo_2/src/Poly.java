import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Poly {
    private ArrayList<VaryPow> exp = new ArrayList<>();

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
        }
        //else if (in.equals(""))
        else if (in.equals("")) {
            VaryPow varyPow = new VaryPow("1");
        }
        else {
            VaryPow varyPow = new VaryPow(in);
            exp.add(varyPow);
            //exp.put(new BigInteger("0"), arrayList);
        }
    }

    public ArrayList<VaryPow> getExp() {
        return exp;
    }

    public Poly getAdd(Poly otherPoly) {
        Iterator<VaryPow> iter1 = this.getExp().iterator();
        Poly polyTemp = new Poly("");
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.getExp().iterator();
            Boolean isUsed = false;
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                /*if (temp1.isTriMerge(temp2)) {
                    polyTemp.getExp().add(temp1.mergeTri(temp2));
                    iter2.remove();
                    isUsed = true;
                    break;
                }*/
                if (temp1.judge(temp2)) {
                    //System.out.println("yes");
                    if (!temp1.getCoEffi().add(temp2.getCoEffi()).equals(BigInteger.ZERO)) {
                        polyTemp.getExp().add(temp1.add(temp2));
                        /*System.out.println("1");

                        System.out.println(temp1.getTriContent());
                        System.out.println(temp2.getTriContent());

                        System.out.println("1");*/
                    }
                    iter2.remove();
                    isUsed = true;
                    break;
                }
            }
            if (isUsed) {
                iter1.remove();
            }
        }
        for (VaryPow temp : this.getExp()) {
            if (!temp.getCoEffi().equals(BigInteger.ZERO)) {
                polyTemp.getExp().add(temp);
            }
        }
        for (VaryPow temp : otherPoly.getExp()) {
            if (!temp.getCoEffi().equals(BigInteger.ZERO)) {
                polyTemp.getExp().add(temp);
            }
        }
        /*for (VaryPow varyPow : polyTemp.getExp()) {
            System.out.println(varyPow.getTriContent());
        }*/
        if (polyTemp.exp.size() == 0) {
            VaryPow varyPow = new VaryPow("0");
            polyTemp.getExp().add(varyPow);
        }
        return polyTemp;
    }

    public Poly getSub(Poly otherPoly) {
        Iterator<VaryPow> iter1 = this.getExp().iterator();
        Poly polyTemp = new Poly("");
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.getExp().iterator();
            Boolean isUsed = false;
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                if (temp1.judge(temp2)) {
                    if (!temp1.getCoEffi().subtract(temp2.getCoEffi()).equals(BigInteger.ZERO)) {
                        polyTemp.getExp().add(temp1.sub(temp2));
                    }
                    iter2.remove();
                    isUsed = true;
                    break;
                }
            }
            if (isUsed) {
                iter1.remove();
            }
        }
        for (VaryPow temp : this.getExp()) {
            if (!temp.getCoEffi().equals(BigInteger.ZERO)) {
                polyTemp.getExp().add(temp);
            }
        }
        for (VaryPow temp : otherPoly.getExp()) {
            temp.setCoEffi(temp.getCoEffi().multiply(BigInteger.valueOf(-1)));
            if (!temp.getCoEffi().equals(BigInteger.ZERO)) {
                polyTemp.getExp().add(temp);
            }
        }
        if (polyTemp.exp.size() == 0) {
            VaryPow varyPow = new VaryPow("0");
            polyTemp.getExp().add(varyPow);
        }
        return polyTemp;
    }

    public Poly getMul(Poly otherPoly) {
        Poly poly = new Poly("0");
        Iterator<VaryPow> iter1 = this.getExp().iterator();
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = otherPoly.getExp().iterator();
            while (iter2.hasNext()) {
                Poly temp = new Poly("");
                VaryPow temp2 = iter2.next();
                temp.getExp().add(temp1.mul(temp2));
                /*for (VaryPow varyPow : temp.getExp()) {
                    System.out.println(varyPow.getTriContent());
                }*/
                poly = poly.getAdd(temp);
            }
        }
        /*for (VaryPow varyPow : poly.getExp()) {
            System.out.println(varyPow.getTriContent());
        }*/
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

    public Poly cos(Poly otherPoly) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cos((");
        stringBuilder.append(otherPoly.toString());
        stringBuilder.append("))");
        HashMap<String, BigInteger> hashMap = new HashMap<>();
        hashMap.put(stringBuilder.toString(), BigInteger.valueOf(1));
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
                BigInteger.ONE, hashMap);
        Poly poly = new Poly("");
        poly.getExp().add(varyPow);
        return poly;
    }

    public Poly sin(Poly otherPoly) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sin((");
        stringBuilder.append(otherPoly.toString());
        stringBuilder.append("))");
        HashMap<String, BigInteger> hashMap = new HashMap<>();
        hashMap.put(stringBuilder.toString(), BigInteger.valueOf(1));
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
                BigInteger.ONE, hashMap);
        Poly poly = new Poly("");
        poly.getExp().add(varyPow);
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
                    if (sb.toString().charAt(0) == '+') {
                        sb.deleteCharAt(0);
                    }
                    sb.append(print2(varyPow));
                    sb.append(print3(varyPow));
                    if (!(varyPow.getXup().equals(BigInteger.ZERO)
                            && varyPow.getYup().equals(BigInteger.ZERO)
                            && varyPow.getZup().equals(BigInteger.ZERO))
                            && !varyPow.getTriContent().isEmpty()) {
                        sb.append("*");
                    }
                    sb.append(print4(varyPow));
                }
            }
        }
        if (sb.toString().length() > 0 && sb.toString().charAt(0) == '+') {
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
        HashMap<String, BigInteger> hs = varyPow.getTriContent();
        if (a.compareTo(BigInteger.valueOf(0)) < 0) {
            if (x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)
                    && z.equals(BigInteger.ZERO) && hs.isEmpty()) {
                sb.append(a);
            }
            else {
                if (a.equals(BigInteger.valueOf(-1))) {
                    sb.append("-");
                }
                else {
                    sb.append(a).append("*");
                }
            }
        }
        else {
            if (x.equals(BigInteger.valueOf(0)) && y.equals(BigInteger.valueOf(0))
                    && z.equals(BigInteger.valueOf(0)) && hs.isEmpty()) {
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
        /*if (x.equals(BigInteger.ZERO) && y.equals(BigInteger.ZERO)
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
        }*/
        return sb.toString();
    }

    public String print3(VaryPow varyPow) {
        StringBuilder sb = new StringBuilder();
        BigInteger x = varyPow.getXup();
        BigInteger y = varyPow.getYup();
        BigInteger z = varyPow.getZup();
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
        /*if (!varyPow.getTriContent().isEmpty()) {
            sb.append("*");
        }*/
        return sb.toString();
    }

    public String print4(VaryPow varyPow) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, BigInteger> hs = varyPow.getTriContent();
        Iterator iter = hs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry temp = (Map.Entry) iter.next();
            if (temp.getValue().equals(BigInteger.ONE)) {
                sb.append(temp.getKey());
            }
            else {
                sb.append(temp.getKey()).append("**").append(temp.getValue());
            }
            if (iter.hasNext()) {
                sb.append("*");
            }
        }
        return sb.toString();
    }
}
