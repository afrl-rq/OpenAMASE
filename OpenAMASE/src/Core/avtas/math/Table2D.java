// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

import avtas.xml.XMLUtil;
import javax.swing.JTable;
import avtas.xml.Element;

/**
 * A class that represents a two dimensional table.
 * @author default
 */
public class Table2D {

    double[][] data;
    int numRows = 1;
    int numCols = 1;
    double[] rowVals;
    double[] colVals;

    /**
     * Creates a new Table2D object.
     * @param rowVals   An array of row indices.
     * @param colVals   An array of column indices.
     */
    public Table2D(double[] rowVals, double[] colVals) {

        this.rowVals = rowVals;
        this.colVals = colVals;
        this.numRows = rowVals.length;
        this.numCols = colVals.length;

        data = new double[numRows][numCols];

    }
    

    /**
     * Gets the number of columns in the table.
     * @return      The number of columns.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Gets the number of rows in the table.
     * @return      The number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Sets the row data in the table.  Only works if the length of the row data
     * array is equal to the number of columns and if the row to set is less than
     * the number of rows.
     * @param row       The row to set.
     * @param rowdata   Data to be put in the row.
     */
    public void setRowData(int row, double[] rowdata) {
        if (rowdata.length == numCols && row < numRows) {
            System.arraycopy(rowdata, 0, data[row], 0, numCols);
        }
    }

    /** Returns the header value for the columns.
     * @return      The column header values.
     */
    public double[] getColVals() {
        return colVals;
    }

    /** Returns the header value fo the rows.
     * @return  The row header values.
     */
    public double[] getRowVals() {
        return rowVals;
    }

    /**
     * Gets the row data for a particular row.
     * @param row       The row number of the row data requested.
     * @return          The row data array.
     */
    public double[] getRowData(int row) {
        return data[row];
    }
    
    /**
     * returns the data array making up the table data
     * @return table data, stored in [row][col]
     */
    public double[][] getTableData() {
        return data;
    }

    /**
     * Get the value in the table given a row and column header.
     * @param row       The row header.
     * @param col       The column header.
     * @return          The value in the table.
     */
    public double getValue(int row, int col) {
        return data[row][col];
    }

    /**
     * Sets the table value given a row and column.
     * @param row       The row header.
     * @param col       The column header.
     * @param value     The value to be put in the table.
     */
    public void setValue(int row, int col, double value) {
        data[row][col] = value;
    }

    /**
     * Multiplies each element in the table by the given scalar.
     * @param val       The multiplication factor.
     */
    public void multiply(double val) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                data[i][j] = data[i][j] * val;
            }
        }
    }

    /**
     * Two dimensional table interpolation given a row value and a column value.
     * @param rowVal        The row value.
     * @param colVal        The column value.
     * @return              The interpolated value.
     */
    public double interp(double rowVal, double colVal) {

        int lowrow = 0;
        int highrow = 0;
        double rowweight = 0;

        int lowcol = 0;
        int highcol = 0;
        double colweight = 0;

        for (int i = 1; i < numRows; i++) {

            lowrow = i - 1;
            highrow = i;
            if (rowVals[i] > rowVal) {
                break;
            }
        }
        if (numRows > 1) {
            rowweight = (rowVal - rowVals[lowrow]) / (rowVals[highrow] - rowVals[lowrow]);
        }

        for (int i = 1; i < numCols; i++) {

            lowcol = i - 1;
            highcol = i;
            if (colVals[i] > colVal) {
                break;
            }
        }
        if (numCols > 1) {
            colweight = (colVal - colVals[lowcol]) / (colVals[highcol] - colVals[lowcol]);
        }

        double firstRow = data[lowrow][lowcol] + (data[lowrow][highcol] - data[lowrow][lowcol]) * colweight;
        double secRow = data[highrow][lowcol] + (data[highrow][highcol] - data[highrow][lowcol]) * colweight;
        return firstRow + (secRow - firstRow) * rowweight;

    }

    /**
     * Gets a string representation of the table.
     * @return      A string representing the table.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();

        //for(int k=0; k<numPages; k++) {
        //buf.append("Page: " + k + "\n");
        for (int i = 0; i < numRows; i++) {
            buf.append("[ ");
            for (int j = 0; j < numCols; j++) {
                buf.append(data[i][j] + ", ");
            }
            buf.append(" ]\n");
        }
        //}
        return buf.toString();
    }

    /**
     * Gets a JTable object populated with the table data.
     * @return      The JTable object.
     */
    public JTable getJTable() {
        Object[][] tableData = new Object[numRows][numCols+1];
        for (int i=0; i<numRows; i++) {
            tableData[i][0] = rowVals[i];
            for (int j=1; j<numCols; j++) {
                tableData[i][j] = data[i][j];
            }
        }
        Object[] colData = new Object[numCols];
        for (int i = 0; i < colData.length; i++) {
            colData[i] = colVals[i];
        }
        JTable table = new JTable(tableData, colData);
        return table;
    }

    public static void main(String[] args) {

        Table2D table = new Table2D(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        table.setRowData(0, new double[]{7, 8, 9});
        table.setRowData(1, new double[]{10, 11, 12});
        table.setRowData(2, new double[]{13, 14, 15});

        System.out.println(table);

        System.out.println(table.interp(20, 4));

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */