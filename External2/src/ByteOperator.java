import mypackage.interfaces.ByteOperable;
import mypackage.interfaces.MetaData;

public class ByteOperator implements ByteOperable {
    @MetaData(metadata = "Performs bitwise and operation on two byte values")
    public Integer and(Byte byte1, Byte byte2) {
        return byte1 & byte2;
    }

    @MetaData(metadata = "Performs bitwise or operation on two byte values")
    public Integer or(Byte byte1, Byte byte2) {
        return byte1 | byte2;
    }

    @MetaData(metadata = "Performs bitwise xor operation on two byte values")
    public Integer xor(Byte byte1, Byte byte2) {
        return byte1 ^ byte2;
    }

    @MetaData(metadata = "Performs bitwise negate operation on byte value")
    public Integer negate(Byte byte1) {
        return ~byte1;
    }
}
