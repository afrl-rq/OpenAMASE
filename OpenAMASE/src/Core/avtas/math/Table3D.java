// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.math;

import javax.swing.JTabbedPane;

/**
 * A class for used to create three dimensional tables.
 * @author default
 */
public class Table3D {
    
    Table2D[] data = null;
    double[] pageVals = null;
    int numPages = 1;
    
    /** Creates a new instance of Table3D.  An array of Table2D objects is used
     *  to implement the three dimensional table.
     *  @param pageVals     An array of page indices.
     *  @param pages        An array of two dimensional tables.
     */
    public Table3D(double[] pageVals, Table2D[] pages) {
        this.pageVals = pageVals;
        numPages = pageVals.length;
        data = pages;
    }
    /**
     * Given a page index, set the associated two dimensional table.
     * @param index     The page index.
     * @param table     The table to be copied into the given page.
     */
    public void setTable(int index, Table2D table) {
        if (index < numPages) {
            data[index] = table;
        }
    }
    /**
     * Get the two dimensional table for a given page.
     * @param index     The page index.
     * @return          The Table2D object representing the page data.
     */
    public Table2D getTable(int index) {
        if (index < numPages) {
            return data[index];
        }
        return null;
    }
    /**
     * Multiply each element in the table by a scalar value.
     * @param val       The multiplication factor.
     */
    public void multiply(double val) {
        for(Table2D table : data) {
            table.multiply(val);
        }
    }

    /**
     * Get the number of pages in the three dimensional table.
     * @return      The number of pages.
     */
    public int getNumberPages() {
        return numPages;
    }
    
    /**
     * returns the values that correspond to the pages (z-axis) of the table.
     */
    public double[] getPageValues() {
        return pageVals;
    }
    
    /**
     * Three dimensional interpolation.  Mirrors and makes use of the interpolation
     * method in the <@link{Table2D Table2D}> class.
     * @param pageVal   The page value.
     * @param rowVal    The row value.
     * @param colVal    The column value.
     * @return          The interpolated value.
     */
    public double interp(double pageVal, double rowVal, double colVal) {
        int lowpage = 0;
        int highpage = 0;
        double pageweight = 0;

        for(int i=1; i<numPages; i++) {
            lowpage = i-1;
            highpage = i;
            if (pageVals[i] > pageVal) {
                break;
            }
        }
        if (numPages > 1)
            pageweight = ( pageVal - pageVals[lowpage] ) / (pageVals[highpage] - pageVals[lowpage]);
        
        double firstpage = data[lowpage].interp(rowVal, colVal);
        double secpage = data[highpage].interp(rowVal, colVal);
        
        return firstpage + (secpage - firstpage) * pageweight;
    }
    
    public static void main(String[] args) {
        Table2D table2d = new Table2D( new double[] {1, 2, 3}, new double[] {1, 2, 3});
        Table3D table3d = new Table3D( new double[] {1}, new Table2D[] {table2d} );
        table2d.setRowData(0, new double[] {1, 2, 3});
        table2d.setRowData(1, new double[] {4, 5, 6});
        table2d.setRowData(2, new double[] {7, 8, 9});
        
        System.out.println(table3d.interp(1, 1, 1));
    }

    /**
     * Gets a <code>JTabbedPane</code> where each tabbed pane is a page in the table.
     * @return  The <code>JTabbedPane</code>
     */
    public JTabbedPane getJTables() {
        JTabbedPane tabPane = new JTabbedPane();
        for (int i = 0; i < numPages; i++) {
            tabPane.addTab(String.valueOf(pageVals[i]), data[i].getJTable());
        }
        return tabPane;
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */