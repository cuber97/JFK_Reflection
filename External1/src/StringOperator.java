import mypackage.interfaces.StringOperable;

public class StringOperator implements StringOperable {
    public String concat(String s1, String s2) {
        return s1 + s2;
    }

    public String toUpperCase(String s) {
        return s.toUpperCase();
    }

    public String toLowerCase(String s) {
        return s.toLowerCase();
    }

    public int length(String s) {
        return s.length();
    }
}
