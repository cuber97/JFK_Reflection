import sample.interfaces.BooleanOperable;

public class BooleanOperator implements BooleanOperable{
    public boolean and(boolean x, boolean y) {
        return x && y;
    }

    public boolean or(boolean x, boolean y) {
        return x || y;
    }

    public boolean xor(boolean x, boolean y) {
        return x ^ y;
    }
}
