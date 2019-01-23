import mypackage.interfaces.ByteOperable;

public class ByteOperator implements ByteOperable {
    public Integer and(Byte byte1, Byte byte2) {
        return byte1 & byte2;
    }

    public Integer or(Byte byte1, Byte byte2) {
        return byte1 | byte2;
    }

    public Integer xor(Byte byte1, Byte byte2) {
        return byte1 ^ byte2;
    }

    public Integer negate(Byte byte1) {
        return ~byte1;
    }
}
