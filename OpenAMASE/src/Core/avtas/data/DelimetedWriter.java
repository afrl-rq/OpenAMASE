// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements a file writer that automatically handles entry delimeters
 * 
 * @author AFRL/RQQD
 */
public class DelimetedWriter {
    
    BufferedWriter writer = null;
    String delimeter = "\t";
    File file = null;
    
    public DelimetedWriter(File f) {
        try {
            this.writer = new BufferedWriter(new FileWriter(f));
        } catch (IOException ex) {
            Logger.getLogger(DelimetedWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DelimetedWriter(File f, String delimeter) {
        this(f);
        this.delimeter = delimeter;
    }
    /**
     * Writes a single value to the file
     * @param value the value to write
     */
    public void write(Object value) {
        try {
            writer.append(String.valueOf(value)).append(delimeter);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(DelimetedWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Creates a new line in the file */
    public void newLine() {
        try {
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(DelimetedWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes a line of data.  Data can be in the form of any object.  This automatically puts the
     *  delimeter after each value and appends a new line.
     * @param values
     */
    public void writeLine(Object... values) {
        try {
            for (Object v : values) {
                if (v instanceof Iterable) {
                    for (Object o : (Iterable) v) {
                        writer.append(String.valueOf(o)).append(delimeter);
                    }
                }
                else {
                    writer.append(String.valueOf(v)).append(delimeter);
                }
            }
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(DelimetedWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Cleans up resources */
    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DelimetedWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */