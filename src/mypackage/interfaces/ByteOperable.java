package mypackage.interfaces;

public interface ByteOperable {
    Integer and(Byte byte1, Byte byte2);
    Integer or(Byte byte1, Byte byte2);
    Integer xor(Byte byte1, Byte byte2);
    Integer negate(Byte byte1);
}
