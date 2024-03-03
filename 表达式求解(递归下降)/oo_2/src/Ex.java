public class Ex {
}
/*public Boolean sinEqual(ArrayList<String> other) {
        VaryPow varyPow1 = new VaryPow();
        varyPow1.setAttri(this.xup, this.yup, this.zup, this.sup, this.cup,
                this.coEffi, this.getSinContent(), this.getCosContent());
        ArrayList<String> other2 = new ArrayList<>();
        for (String str : other) {
            other2.add(str);
        }
        Iterator<String> iter1 = varyPow1.sinContent.iterator();
        while (iter1.hasNext()) {
            Iterator<String> iter2 = other2.iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                if (iter1.equals(iter2)) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (!used) {
                return false;
            }
            iter1.remove();
        }
        if ((!other.isEmpty()) || (!varyPow1.getSinContent().isEmpty())) {
            return false;
        }
        return true;
    }*/

    /*public Boolean cosEqual(ArrayList<String> other) {
        VaryPow varyPow1 = new VaryPow();
        varyPow1.setAttri(this.xup, this.yup, this.zup, this.sup, this.cup,
                this.coEffi, this.getSinContent(), this.getCosContent());
        ArrayList<String> other2 = new ArrayList<>();
        for (String str : other) {
            other2.add(str);
        }
        Iterator<String> iter1 = varyPow1.cosContent.iterator();
        while (iter1.hasNext()) {
            Iterator<String> iter2 = other2.iterator();
            Boolean used = false;
            while (iter2.hasNext()) {
                if (iter1.equals(iter2)) {
                    used = true;
                    iter2.remove();
                    break;
                }
            }
            if (!used) {
                return false;
            }
            iter1.remove();
        }
        if ((!other.isEmpty()) || (!varyPow1.getCosContent().isEmpty())) {
            return false;
        }
        return true;
    }*/

    /*public Boolean isTriMerge(VaryPow otherVaryPow) {
        if (this.getXup().equals(otherVaryPow.getXup())
                && this.getYup().equals(otherVaryPow.getYup())
                && this.getZup().equals(otherVaryPow.getZup())
                && this.getCoEffi().equals(otherVaryPow.getCoEffi())) {
            if (this.getSup().equals(BigInteger.valueOf(2)) && this.getCup().equals(BigInteger.ZERO)
                    && otherVaryPow.getSup().equals(BigInteger.ZERO)
                    && otherVaryPow.getCup().equals(BigInteger.valueOf(2))
                    && (this.getSinContent().size() == 1)
                    && (otherVaryPow.getCosContent().size() == 1)
                    && (this.getSinContent().get(0).equals(otherVaryPow.getCosContent().get(0)))) {
                return true;
            }
            if (this.getCup().equals(BigInteger.valueOf(2)) && this.getSup().equals(BigInteger.ZERO)
                    && otherVaryPow.getSup().equals(BigInteger.valueOf(2))
                    && otherVaryPow.getCup().equals(BigInteger.ZERO)
                    && (this.getCosContent().size() == 1)
                    && (otherVaryPow.getSinContent().size() == 1)
                    && (this.getCosContent().get(0).equals(otherVaryPow.getSinContent().get(0)))) {
                return true;
            }
        }
        return false;
    }*/

    /*public VaryPow mergeTri(VaryPow otherVaryPow) {
        VaryPow varyPow = new VaryPow();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> temp1 = new ArrayList<>();
        varyPow.setAttri(this.xup, this.yup, this.zup,
                BigInteger.ZERO, BigInteger.ZERO, this.getCoEffi(), temp, temp1);
        return varyPow;
    }*/