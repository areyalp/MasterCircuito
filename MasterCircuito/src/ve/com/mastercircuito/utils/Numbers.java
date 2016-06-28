package ve.com.mastercircuito.utils;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class Numbers {
	public static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	
	public static boolean isNumeric(char c)
	{
		NumberFormat formatter = NumberFormat.getInstance();
		  ParsePosition pos = new ParsePosition(0);
		  formatter.parse(String.valueOf(c), pos);
		  return 1 == pos.getIndex();
	}
}
