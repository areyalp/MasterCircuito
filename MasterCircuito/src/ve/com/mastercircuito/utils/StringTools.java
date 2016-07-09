package ve.com.mastercircuito.utils;

public class StringTools {
	
	public static String removeFirstChar(String s) {
	    if (s == null || s.length() == 0) {
	        return s;
	    }
	    return s.substring(1, s.length());
	}
	
	public static String removeLastChar(String s) {
	    if (s == null || s.length() == 0) {
	        return s;
	    }
	    return s.substring(0, s.length()-1);
	}
	
	public static String implode(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
		return String.join(delimiter, elements);
	}

}
