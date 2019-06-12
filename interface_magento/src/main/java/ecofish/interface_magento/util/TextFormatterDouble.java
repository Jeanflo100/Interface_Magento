package ecofish.interface_magento.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 * Provides the writing format to be used for double variable
 * @author Jean-Florian Tassart
 */
public class TextFormatterDouble {

    private DecimalFormat df ;
    private TextFormatter<Double> textFormatter ;
    
    /**
     * Initialization of the format
     */
    public TextFormatterDouble() {
        Locale locale  = new Locale("en", "EN");
        String pattern = "###,###.##";
        df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyPattern(pattern);

        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public String toString(Double number) {
            	if (number == 0.0) return "";
            	else return df.format(number);
            }
            @Override
            public Double fromString(String string) {
                try {
                    if (string.equals("")) return 0.0;
                    else return df.parse(string).doubleValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0.0 ;
                }
            }
        };

        ParsePosition parsePosition = new ParsePosition(0);
        textFormatter = new TextFormatter<>(converter, 0.0, c ->
        {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            parsePosition.setIndex(0);
            Object object = df.parse(c.getControlNewText(), parsePosition);
            Integer index_decimal = c.getControlNewText().indexOf(".");
            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() ||
            		(index_decimal != -1 && c.getControlNewText().substring(index_decimal + 1).length() > 2)) {
                return null;
            }
            else {
                return c;
            }
        });
    }
    
    /**
     * Provides the instance of the format
     * @return Instance of the format
     */
    public TextFormatter<Double> getTextFormatterDouble(){
    	return textFormatter;
    }
    
}