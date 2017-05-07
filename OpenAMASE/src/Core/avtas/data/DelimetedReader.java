// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a File reader that automatically handles files delimeters
 * @author AFRL/RQQD
 */
public class DelimetedReader {
    
    BufferedReader reader = null;
    String delimeter = "\t";
    File file = null;

    public DelimetedReader(File f, String delimeter) {
        
        try {
            
            this.file = f;
            this.delimeter = delimeter;
            this.reader = new BufferedReader(new FileReader(file));

        } catch (IOException ex) {
            Logger.getLogger(DelimetedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /** Attempts to read the requested line.  If the line is beyond the end of the file, then
     * this returns null.  Note: To make sequential reads on the file, it is more efficient to
     * reset the reader and call readNextLine().
     *
     * This does not affect the position of the default reader (the one used by readNextLine())
     *
     * @param row the row number to read (starting with zero)
     * @return an array of strings representing the values at the requested row.
     */
    public String[] readLine(int row) {
        try {
            BufferedReader tmpReader = new BufferedReader(new FileReader(file));
            for (int i = 0; i < row; i++) {
                tmpReader.readLine();
            }
            String rowVal = tmpReader.readLine();
            return rowVal.split(delimeter);
        } catch (IOException ex) {
            Logger.getLogger(DelimetedReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /** Attempts to read the next line of data.  If the reader is at the end of the file, this
     * returns null.
     * @return an array of strings representing the data read
     */
    public String[] readNextLine() {
        try {
            String line = reader.readLine();
            if (line == null ) {
                return null;
            }
            return line.split(delimeter);
        } catch (IOException ex) {
            Logger.getLogger(DelimetedReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /** Sets the reader to the requested row */
    public void setReader(int row) {
        try {
            reader.reset();
            for (int i = 0; i < row; i++) {
                reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(DelimetedReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /** Cleans up resources */
    public void close() {
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(DelimetedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */