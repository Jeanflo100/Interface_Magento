package ecofish.interface_magento.util;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import ecofish.interface_magento.model.Product;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;


public class EditingDoubleCell extends TableCell<Product,Double>{
	
    private TextField textField;
    private TextFormatter<Double> textFormatter ;

    private DecimalFormat df ;
    
    public EditingDoubleCell() {
        Locale locale  = new Locale("en", "EN");
        String pattern = "###,###.##";
        df = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        df.applyPattern(pattern);
        //this.getTableView().getSelectionModel().getSelectedItem();
    }
    
    @Override
    public void startEdit() {
        //if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setGraphic(textField);
            textField.requestFocus();
        //}
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }


    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());

                }
                setGraphic(textField);
            } else {
                setGraphic(null);
            }
        }
    }

    private String getString() {
        return getItem() == null ? "" : df.format(getItem());
    }

    private void createTextField(){

        textField = new TextField();

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public String toString(Double number) {
                return df.format(number);
            }

            @Override
            public Double fromString(String string) {
                try {
                	Double value = 0.0;
                    if (!string.equals("")) value = df.parse(string).doubleValue();
                    return value;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0.0 ;
                }
            }

        };

        textFormatter = new TextFormatter<>(converter, 0.0, c ->
        {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = df.parse(c.getControlNewText(), parsePosition);

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        } ) ;

        // add filter to allow for typing only integer
        textField.setTextFormatter( textFormatter);

        textField.setText( getString() );

        textField.setMaxWidth( this.getWidth() - this.getGraphicTextGap() * 2 );

        // commit on Enter
        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            commitEdit(newValue);
        });
    }
}