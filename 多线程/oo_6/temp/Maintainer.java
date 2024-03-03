import java.util.HashMap;

public class Maintainer {
    private static HashMap<Integer, Elevator> elevators;

    public Maintainer() {
        elevators = new HashMap<>();
    }

    public HashMap<Integer, Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(HashMap<Integer, Elevator> elevators) {
        this.elevators = elevators;
    }

    public void putEle(Integer integer, Elevator elevator) {
        this.elevators.put(integer, elevator);
    }

    public static boolean haveExtraPas() {
        for (Elevator elevator : elevators.values()) {
            if (!elevator.getCurPersons().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
