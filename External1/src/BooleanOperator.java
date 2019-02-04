import mypackage.interfaces.BooleanOperable;
import mypackage.interfaces.MetaData;

public class BooleanOperator implements BooleanOperable{
    @MetaData(metadata = "Performs logical and operation on two boolean values")
    public boolean and(boolean x, boolean y) { return x && y; }

    @MetaData(metadata = "Performs logical or operation on two byte values")
    public boolean or(boolean x, boolean y) {
        return x || y;
    }

    @MetaData(metadata = "Performs logical xor operation on two byte values")
    public boolean xor(boolean x, boolean y) {
        return x ^ y;
    }
}
