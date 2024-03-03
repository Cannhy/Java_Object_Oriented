import javafx.util.Pair;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VaryPow  implements Serializable {
    private BigInteger xup = BigInteger.ZERO;
    private BigInteger yup = BigInteger.ZERO;
    private BigInteger zup = BigInteger.ZERO;

    private BigInteger coEffi = BigInteger.ZERO;

    private HashMap<Poly, Pair<BigInteger, Boolean>> triContent = new HashMap<>();

    public VaryPow(String in) {
        this.setCoEffi(new BigInteger(in));
    }

    public VaryPow() {
    }

    public void setAttri(BigInteger xup, BigInteger yup, BigInteger zup,
                         BigInteger coEffi, HashMap<Poly, Pair<BigInteger, Boolean>> triContent) {
        this.coEffi = coEffi;
        this.xup = (coEffi.equals(BigInteger.ZERO)) ? BigInteger.valueOf(0) : xup;
        this.yup = (coEffi.equals(BigInteger.ZERO)) ? BigInteger.valueOf(0) : yup;
        this.zup = (coEffi.equals(BigInteger.ZERO)) ? BigInteger.valueOf(0) : zup;
        this.triContent = triContent;
    }

    public Boolean judge(VaryPow otherVaryPow) throws IOException, ClassNotFoundException {
        if (!this.getXup().equals(otherVaryPow.getXup()) ||
                !this.getYup().equals(otherVaryPow.getYup()) ||
                !this.getZup().equals(otherVaryPow.getZup())) {
            return false;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        /*VaryPow varyPow1 = (VaryPow) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();*/
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
        oos1.writeObject(otherVaryPow);
        ByteArrayInputStream bis1 = new ByteArrayInputStream(bos1.toByteArray());
        ObjectInputStream ois1 = new ObjectInputStream(bis1);
        VaryPow varyPow2 = (VaryPow) ois1.readObject();//从流中把数据读出来
        VaryPow varyPow1 = (VaryPow) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();
        Iterator iter1 = varyPow1.getTriContent().entrySet().iterator();
        bos1.close();
        bis1.close();
        while (iter1.hasNext()) {
            Map.Entry temp1 = (Map.Entry) iter1.next();
            Iterator iter2 = varyPow2.getTriContent().entrySet().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                Map.Entry temp2 = (Map.Entry) iter2.next();
                if (((Pair<BigInteger, Boolean>)temp1.getValue()).getKey().
                        equals(((Pair<BigInteger, Boolean>)temp2.getValue()).getKey()) &&
                        ((Pair<BigInteger, Boolean>)temp1.getValue()).getValue().
                        equals(((Pair<BigInteger, Boolean>)temp2.getValue()).getValue()) &&
                        (((Poly)temp1.getKey()).equal((Poly)temp2.getKey()))) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        if (!varyPow1.getTriContent().isEmpty() || !varyPow2.getTriContent().isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean judge2(VaryPow otherVaryPow) throws IOException, ClassNotFoundException {
        if (!this.getXup().equals(otherVaryPow.getXup()) ||
                !this.getYup().equals(otherVaryPow.getYup()) ||
                !this.getZup().equals(otherVaryPow.getZup()) ||
                !this.getCoEffi().equals(otherVaryPow.getCoEffi())) {
            return false;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        /*VaryPow varyPow1 = (VaryPow) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();*/
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
        oos1.writeObject(otherVaryPow);
        ByteArrayInputStream bis1 = new ByteArrayInputStream(bos1.toByteArray());
        ObjectInputStream ois1 = new ObjectInputStream(bis1);
        VaryPow varyPow2 = (VaryPow) ois1.readObject();//从流中把数据读出来
        VaryPow varyPow1 = (VaryPow) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();
        Iterator iter1 = varyPow1.getTriContent().entrySet().iterator();
        bos1.close();
        bis1.close();
        while (iter1.hasNext()) {
            Map.Entry temp1 = (Map.Entry) iter1.next();
            Iterator iter2 = varyPow2.getTriContent().entrySet().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                Map.Entry temp2 = (Map.Entry) iter2.next();
                if (((Pair<BigInteger, Boolean>)temp1.getValue()).getKey().
                        equals(((Pair<BigInteger, Boolean>)temp2.getValue()).getKey()) &&
                        ((Pair<BigInteger, Boolean>)temp1.getValue()).getValue().
                                equals(((Pair<BigInteger, Boolean>)temp2.getValue()).getValue()) &&
                        (((Poly)temp1.getKey()).equal((Poly)temp2.getKey()))) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        if (!varyPow1.getTriContent().isEmpty() || !varyPow2.getTriContent().isEmpty()) {
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

    public VaryPow mul(VaryPow otherVaryPow) throws IOException, ClassNotFoundException {
        VaryPow varyPow1 = deepCop(this);
        VaryPow varyPow2 = deepCop(otherVaryPow);//从流中把数据读出来
        HashMap<Poly, Pair<BigInteger, Boolean>> temp = new HashMap<>();
        Iterator iter1 = varyPow1.getTriContent().entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry temp1 = (Map.Entry) iter1.next();
            Iterator iter2 = varyPow2.getTriContent().entrySet().iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                Map.Entry temp2 = (Map.Entry) iter2.next();
                if (((Poly)temp1.getKey()).equal((Poly) temp2.getKey()) &&
                        ((Pair<BigInteger, Boolean>)temp1.getValue()).getValue()
                                .equals(((Pair<BigInteger, Boolean>)temp2.getValue()).getValue())) {
                    //System.out.println("in");
                    used = true;
                    temp.put((Poly) temp1.getKey(),
                            new Pair<>(((Pair<BigInteger, Boolean>) temp1.getValue()).
                            getKey().add(((Pair<BigInteger, Boolean>) temp2.getValue()).getKey()),
                            ((Pair<BigInteger, Boolean>) temp1.getValue()).getValue()));
                    iter2.remove();
                    break;
                }
            }
            if (used) {
                iter1.remove();
            }
        }
        for (Map.Entry<Poly, Pair<BigInteger, Boolean>> str : varyPow1.getTriContent().entrySet()) {
            temp.put(str.getKey(), str.getValue());
        }
        for (Map.Entry<Poly, Pair<BigInteger, Boolean>> str : varyPow2.getTriContent().entrySet()) {
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

    public VaryPow deepCop(VaryPow des) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(des);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        VaryPow varyPow1 = (VaryPow) ois.readObject();//从流中把数据读出来
        bos.close();
        bis.close();
        return varyPow1;
    }

    public Poly diff(String toDiff) throws IOException, ClassNotFoundException {
        Poly poly = new Poly("0");
        VaryPow varyPow1 = deepCop(this);
        if ((toDiff.equals("x") && !this.xup.equals(BigInteger.ZERO))
                || (toDiff.equals("y") && !this.yup.equals(BigInteger.ZERO))
                || (toDiff.equals("z") && !this.zup.equals(BigInteger.ZERO))) {
            poly.getExp().add(xyzDiff(toDiff));
        }
        for (Map.Entry me : varyPow1.getTriContent().entrySet()) {
            VaryPow varyPoweach = new VaryPow();
            varyPoweach = deepCop(this);
            //varyPoweach.getTriContent().remove((Poly) me.getKey());
            //先移走这个三角函数 再把它求导之后得到的东西乘进来 放进来
            Poly polyCopy = (Poly) me.getKey();    // 三角函数的内容是polyCopy
            BigInteger pow = ((Pair<BigInteger, Boolean>)me.getValue()).getKey();
            Boolean type = ((Pair<BigInteger, Boolean>)me.getValue()).getValue();
            for (Poly py : varyPoweach.getTriContent().keySet()) {
                if (py.equal(polyCopy) && varyPoweach.getTriContent().get(py).
                        getKey().equals(pow)
                        && varyPoweach.getTriContent().get(py).getValue().equals(type)) {
                    varyPoweach.getTriContent().remove(py);
                    break;
                }
            }
            if (pow.equals(BigInteger.ONE)) {
                Pair<BigInteger, Boolean> temp = new Pair<>(pow, !type.booleanValue());
                varyPoweach.getTriContent().put(polyCopy, temp);
                if (type.booleanValue()) {
                    varyPoweach.setCoEffi(varyPoweach.getCoEffi().
                            multiply(BigInteger.valueOf(-1)));
                }
                Poly polyTemp = polyCopy.getDiff(toDiff); //polyTemp是内容求导
                Poly total = new Poly("");
                total.getExp().add(varyPoweach);
                total = polyTemp.getMul(total);
                poly = poly.getAdd(total);
            } else {
                varyPoweach.setCoEffi(varyPoweach.getCoEffi().multiply(pow));
                Pair<BigInteger, Boolean> temp = new Pair<>(pow.subtract(BigInteger.ONE),
                        type.booleanValue());
                varyPoweach.getTriContent().put(polyCopy, temp);
                VaryPow tri = new VaryPow();
                HashMap<Poly, Pair<BigInteger, Boolean>> sec = new HashMap<>();
                sec.put(polyCopy, new Pair<>(BigInteger.ONE, type));
                tri.setAttri(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO,
                        BigInteger.ONE, sec);
                Poly next = new Poly("");
                next.getExp().add(varyPoweach);
                next = next.getMul(tri.diff(toDiff));
                poly = poly.getAdd(next);
            }
        }


        return poly;
    }

    public VaryPow xyzDiff(String toDiff) throws IOException, ClassNotFoundException {
        VaryPow varyPow1 = deepCop(this);
        if (toDiff.equals("x")) {
            if (!this.xup.equals(BigInteger.ZERO)) {
                VaryPow varyPow2 = new VaryPow();
                varyPow2.setAttri(varyPow1.getXup().subtract(BigInteger.ONE),
                        varyPow1.getYup(), varyPow1.getZup(),
                        varyPow1.getCoEffi().multiply(varyPow1.getXup()), varyPow1.getTriContent());
                return varyPow2;
            }
        } else if (toDiff.equals("y")) {
            if (!this.yup.equals(BigInteger.ZERO)) {
                VaryPow varyPow2 = new VaryPow();
                varyPow2.setAttri(varyPow1.getXup(),
                        varyPow1.getYup().subtract(BigInteger.ONE), varyPow1.getZup(),
                        varyPow1.getCoEffi().multiply(varyPow1.getYup()), varyPow1.getTriContent());
                return varyPow2;
            }
        } else if (toDiff.equals("z")) {
            if (!this.zup.equals(BigInteger.ZERO)) {
                VaryPow varyPow2 = new VaryPow();
                varyPow2.setAttri(varyPow1.getXup(),
                        varyPow1.getYup(), varyPow1.getZup().subtract(BigInteger.ONE),
                        varyPow1.getCoEffi().multiply(varyPow1.getZup()), varyPow1.getTriContent());
                return varyPow2;
            }
        }
        return new VaryPow();
    }

    @Override
    public String toString() {
        BigInteger a = this.getCoEffi();
        BigInteger x = this.getXup();
        BigInteger y = this.getYup();
        BigInteger z = this.getZup();
        StringBuilder sb = new StringBuilder();
        HashMap<Poly, Pair<BigInteger, Boolean>> hs = this.getTriContent();
        if (a.compareTo(BigInteger.ZERO) < 0) {
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
        /*if (sb.toString().charAt(0) == '+') {
            sb.deleteCharAt(0);
        }*/
        sb.append(toString1());
        sb.append(toString2());
        if (!(this.getXup().equals(BigInteger.ZERO)
                && this.getYup().equals(BigInteger.ZERO)
                && this.getZup().equals(BigInteger.ZERO))
                && !this.getTriContent().isEmpty()) {
            sb.append("*");
        }
        sb.append(toString3());
        return sb.toString();
    }

    public String toString1() {
        BigInteger a = this.getCoEffi();
        BigInteger x = this.getXup();
        BigInteger y = this.getYup();
        BigInteger z = this.getZup();
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    public String toString2() {
        StringBuilder sb = new StringBuilder();
        BigInteger x = this.getXup();
        BigInteger y = this.getYup();
        BigInteger z = this.getZup();
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

    public String toString3() {
        StringBuilder sb = new StringBuilder();
        HashMap<Poly, Pair<BigInteger, Boolean>> hs = this.getTriContent();
        Iterator iter = hs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry temp = (Map.Entry) iter.next();
            if (((Pair<BigInteger, Boolean>)temp.getValue()).getKey().equals(BigInteger.ONE)) {
                if (((Pair<BigInteger, Boolean>)temp.getValue()).getValue()) {
                    sb.append("cos((").append(((Poly) temp.getKey()).toString()).append("))");
                } else {
                    sb.append("sin((").append(((Poly) temp.getKey()).toString()).append("))");
                }
            } else {
                if (((Pair<BigInteger, Boolean>)temp.getValue()).getValue()) {
                    sb.append("cos((").append(((Poly) temp.getKey()).toString())
                            .append("))").append("**").
                            append(((Pair<BigInteger, Boolean>)temp.getValue()).getKey());
                } else {
                    sb.append("sin((").append(((Poly) temp.getKey()).toString())
                            .append("))").append("**").
                            append(((Pair<BigInteger, Boolean>)temp.getValue()).getKey());
                }
            }
            if (iter.hasNext()) {
                sb.append("*");
            }
        }
        return sb.toString();
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

    public HashMap<Poly, Pair<BigInteger, Boolean>> getTriContent() {
        return triContent;
    }

    public void setTriContent(HashMap<Poly, Pair<BigInteger, Boolean>> triContent) {
        this.triContent = triContent;
    }
}
