import java.util.ArrayList;
import java.util.HashMap;

public class SdFunc {
    private HashMap<String, ArrayList<String>> funcTion = new HashMap<>();
    //private HashMap<String, Integer> Number = new HashMap<>();

    public SdFunc() {
    }

    public void addFun(String input) {
        ArrayList<String> arrayList = new ArrayList<>();
        String para = input.split("=")[0];
        //System.out.println(para);
        String cont = input.split("=")[1];
        //System.out.println(cont);
        for (int i = para.indexOf('(') + 1; i < para.length() - 1; i++) {
            if (para.charAt(i) != ',') {
                arrayList.add(para.substring(i, i + 1));
            }
        }
        arrayList.add(cont);
        this.funcTion.put(input.substring(0, 1), arrayList);
    }

    public String replac(String input, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String[] temp = input.split(" ");
        ArrayList<String> fun = this.funcTion.get(type);
        Boolean usd = false;
        /*System.out.println(fun.get(0));
        System.out.println(fun.get(fun.size() - 1).charAt(0));
        System.out.println(fun.get(fun.size() - 1).charAt(1));
        System.out.println(fun.get(fun.size() - 1).charAt(2));*/
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

    /*public String reFunc(String from) {

    }*/

    public HashMap<String, ArrayList<String>> getFuncTion() {
        return funcTion;
    }

    public void setFuncTion(HashMap<String, ArrayList<String>> funcTion) {
        this.funcTion = funcTion;
    }
}
