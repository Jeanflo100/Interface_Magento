package ecofish.interface_magento.util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DetailsTableView {
	
    /**
     * Returns the number of rows of the table visible on the screen
     * @param table - table concerned
     * @return The number of visible rows
     */
	public static Integer getNumberVisibleRow(TableView<?> table) {
		Integer numberColumnRow = 0;
		Integer newNumberColumnRow = numberColumnRow;
		for (TableColumn<?, ?> column : table.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(column, numberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		numberColumnRow = newNumberColumnRow;
		Integer numberVisibleRow = (int) ((table.getPrefHeight() - numberColumnRow * 20) / table.getFixedCellSize());
		return numberVisibleRow;
	}
	
	/**
	 * Recursive function returning the number of column rows
	 * @param column - column currently concerned
	 * @param actualNumberColumnRow - current number of subcolumn rows
	 * @return Total number of subcolumn rows
	 */
	private static Integer getNumberColumnRow(TableColumn<?, ?> column, Integer actualNumberColumnRow) {
		Integer newNumberColumnRow = actualNumberColumnRow;
		for (TableColumn<?, ?> subColumn : column.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(subColumn, actualNumberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		return newNumberColumnRow + 1;
	}
	
}