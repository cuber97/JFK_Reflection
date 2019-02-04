import mypackage.interfaces.IntegerOperable;
import mypackage.interfaces.MetaData;

public class IntegerOperator implements IntegerOperable {
    @MetaData(metadata = "Adds two integer values")
    public int add(int a, int b) {
        return a + b;
    }

    @MetaData(metadata = "Subtracts two integer values")
    public int subtract(int a, int b) {
        return a - b;
    }

    @MetaData(metadata = "Multiplies two integer values")
    public int multiply(int a, int b) {
        return a * b;
    }

    @MetaData(metadata = "Finds maximum value of two integers")
    public int max(int a, int b) {
        return Math.max(a, b);
    }

    @MetaData(metadata = "Finds minimum value of two integers")
    public int min(int a, int b) {
        return Math.min(a, b);
    }
}
