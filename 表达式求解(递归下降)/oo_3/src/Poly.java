import javafx.util.Pair;

import java.io.Serializable;
import java.math.BigInteger;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Poly implements Serializable {
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

    public Poly getAdd(Poly otherPoly) throws IOException, ClassNotFoundException {
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
                        polyTemp.getExp().add(temp1.add(temp2));        //twice fix bug
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

    public Poly getSub(Poly otherPoly) throws IOException, ClassNotFoundException {
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
                    }          // first fix bug
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

    public Poly getMul(Poly otherPoly) throws IOException, ClassNotFoundException {
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

    public Poly power(Poly otherPoly) throws IOException, ClassNotFoundException {
        Poly poly = new Poly("1");
        BigInteger count = new BigInteger("0");
        while (count.compareTo(otherPoly.getExp().get(0).getCoEffi()) < 0) {
            poly = poly.getMul(this);
            count = count.add(BigInteger.valueOf(1));
        }
        return poly;
    }

    public Poly cos(Poly otherPoly) {
        HashMap<Poly, Pair<BigInteger, Boolean>> hashMap = new HashMap<>();
        hashMap.put(otherPoly, new Pair<>(BigInteger.ONE, Boolean.TRUE));
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
                BigInteger.ONE, hashMap);
        Poly poly = new Poly("");
        poly.getExp().add(varyPow);
        return poly;
    }

    public Poly sin(Poly otherPoly) {
        HashMap<Poly, Pair<BigInteger, Boolean>> hashMap = new HashMap<>();
        hashMap.put(otherPoly, new Pair<>(BigInteger.ONE, Boolean.FALSE));
        VaryPow varyPow = new VaryPow();
        varyPow.setAttri(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
                BigInteger.ONE, hashMap);
        Poly poly = new Poly("");
        poly.getExp().add(varyPow);
        return poly;
    }

    public Poly getDiff(String diffType) throws IOException, ClassNotFoundException {
        Poly poly = new Poly("0");
        Iterator<VaryPow> iter = this.getExp().iterator();
        while (iter.hasNext()) {
            VaryPow varyPow = iter.next();
            poly = poly.getAdd(varyPow.diff(diffType));
        }
        return poly;
    }

    public boolean equal(Poly o) throws IOException, ClassNotFoundException {
        Poly poly = (Poly) o;
        Poly poly1 = deepCop(this);
        Poly poly2 = deepCop(o);
        Iterator<VaryPow> iter1 = poly1.getExp().iterator();
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = poly2.getExp().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                if (((VaryPow)temp1).judge((VaryPow) temp2)) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        if (!poly1.getExp().isEmpty() || !poly2.getExp().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean equal2(Poly o) throws IOException, ClassNotFoundException {
        Poly poly = (Poly) o;
        Poly poly1 = deepCop(this);
        Poly poly2 = deepCop(o);
        Iterator<VaryPow> iter1 = poly1.getExp().iterator();
        while (iter1.hasNext()) {
            VaryPow temp1 = iter1.next();
            Iterator<VaryPow> iter2 = poly2.getExp().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                VaryPow temp2 = iter2.next();
                if (((VaryPow)temp1).judge2((VaryPow) temp2)) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        if (!poly1.getExp().isEmpty() || !poly2.getExp().isEmpty()) {
            return false;
        }
        return true;
    }

    public Poly deepCop(Poly des) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(des);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Poly poly = (Poly) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();
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
                    sb.append(varyPow);
                }
            }
        }
        if (sb.toString().length() > 0 && sb.toString().charAt(0) == '+') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /*public String print4(VaryPow varyPow) {
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
    }*/
}
