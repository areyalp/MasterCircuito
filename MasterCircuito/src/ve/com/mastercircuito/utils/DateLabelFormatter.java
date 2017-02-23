package ve.com.mastercircuito.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4060563638372355547L;
	private String datePattern = "dd-MM-yyyy";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    
    public DateLabelFormatter() {
    	super();
    	this.datePattern = "dd-MM-yyyy";
    }
    
    public DateLabelFormatter(String datePattern) {
    	super();
    	this.datePattern = datePattern;
    }
    
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
         
        return "";
    }

}
