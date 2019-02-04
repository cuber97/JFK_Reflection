import mypackage.interfaces.MetaData;
import mypackage.interfaces.StringOperable;

public class StringOperator implements StringOperable {
    @MetaData(metadata = "Concats two strings given as arguments")
    public String concat(String s1, String s2) {
        return s1 + s2;
    }

    @MetaData(metadata = "Makes every sign of given char uppercase")
    public String toUpperCase(String s) {
        return s.toUpperCase();
    }

    @MetaData(metadata = "Makes every sign of given char lowercase")
    public String toLowerCase(String s) {
        return s.toLowerCase();
    }

    @MetaData(metadata = "Returns length of string")
    public int length(String s) {
        return s.length();
    }
}
