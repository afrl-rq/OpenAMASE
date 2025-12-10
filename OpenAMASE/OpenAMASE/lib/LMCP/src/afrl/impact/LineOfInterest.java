// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Line of interest 
*/
public class LineOfInterest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 20;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "LineOfInterest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.LineOfInterest";

    /**  ID for lines of interest (Units: None)*/
    @LmcpType("int64")
    protected long LineID = 0L;
    /**  List of points to search (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> Line = new java.util.ArrayList<afrl.cmasi.Location3D>();
    /**  Action that updated this line of interest (e.g. created, destroyed, modified) (Units: None)*/
    @LmcpType("AreaActionOptions")
    protected afrl.impact.AreaActionOptions LineAction = afrl.impact.AreaActionOptions.Created;
    /**  Human readable label for line of interest (Units: None)*/
    @LmcpType("string")
    protected String LineLabel = "";
    /**  Background Behavior line is true if point is for background behavior (Units: None)*/
    @LmcpType("bool")
    protected boolean BackgroundBehaviorLine = false;

    
    public LineOfInterest() {
    }

    public LineOfInterest(long LineID, afrl.impact.AreaActionOptions LineAction, String LineLabel, boolean BackgroundBehaviorLine){
        this.LineID = LineID;
        this.LineAction = LineAction;
        this.LineLabel = LineLabel;
        this.BackgroundBehaviorLine = BackgroundBehaviorLine;
    }


    public LineOfInterest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            LineOfInterest newObj = new LineOfInterest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID for lines of interest (Units: None)*/
    public long getLineID() { return LineID; }

    /**  ID for lines of interest (Units: None)*/
    public LineOfInterest setLineID( long val ) {
        LineID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Location3D> getLine() {
        return Line;
    }

    /**  Action that updated this line of interest (e.g. created, destroyed, modified) (Units: None)*/
    public afrl.impact.AreaActionOptions getLineAction() { return LineAction; }

    /**  Action that updated this line of interest (e.g. created, destroyed, modified) (Units: None)*/
    public LineOfInterest setLineAction( afrl.impact.AreaActionOptions val ) {
        LineAction = val;
        return this;
    }

    /**  Human readable label for line of interest (Units: None)*/
    public String getLineLabel() { return LineLabel; }

    /**  Human readable label for line of interest (Units: None)*/
    public LineOfInterest setLineLabel( String val ) {
        LineLabel = val;
        return this;
    }

    /**  Background Behavior line is true if point is for background behavior (Units: None)*/
    public boolean getBackgroundBehaviorLine() { return BackgroundBehaviorLine; }

    /**  Background Behavior line is true if point is for background behavior (Units: None)*/
    public LineOfInterest setBackgroundBehaviorLine( boolean val ) {
        BackgroundBehaviorLine = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 13; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Line);
        size += LMCPUtil.sizeOfString(LineLabel);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        LineID = LMCPUtil.getInt64(in);

        Line.clear();
        int Line_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Line_len; i++){
        Line.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }
        LineAction = afrl.impact.AreaActionOptions.unpack( in );

        LineLabel = LMCPUtil.getString(in);

        BackgroundBehaviorLine = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, LineID);
        LMCPUtil.putUint16(out, Line.size());
        for(int i=0; i<Line.size(); i++){
            LMCPUtil.putObject(out, Line.get(i));
        }
        LineAction.pack(out);
        LMCPUtil.putString(out, LineLabel);
        LMCPUtil.putBool(out, BackgroundBehaviorLine);

    }

    public int getLMCPType() { return LMCP_TYPE; }

    public String getLMCPSeriesName() { return SERIES_NAME; }

    public long getLMCPSeriesNameAsLong() { return SERIES_NAME_ID; }

    public int getLMCPSeriesVersion() { return SERIES_VERSION; };

    public String getLMCPTypeName() { return TYPE_NAME; }

    public String getFullLMCPTypeName() { return FULL_LMCP_TYPE_NAME; }

    public String toString() {
        return toXML("");
    }

    public String toXML(String ws) {
        StringBuffer buf = new StringBuffer();
        buf.append( ws + "<LineOfInterest Series=\"IMPACT\">\n");
        buf.append( ws + "  <LineID>" + String.valueOf(LineID) + "</LineID>\n");
        buf.append( ws + "  <Line>\n");
        for (int i=0; i<Line.size(); i++) {
            buf.append( Line.get(i) == null ? ( ws + "    <null/>\n") : (Line.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Line>\n");
        buf.append( ws + "  <LineAction>" + String.valueOf(LineAction) + "</LineAction>\n");
        buf.append( ws + "  <LineLabel>" + String.valueOf(LineLabel) + "</LineLabel>\n");
        buf.append( ws + "  <BackgroundBehaviorLine>" + String.valueOf(BackgroundBehaviorLine) + "</BackgroundBehaviorLine>\n");
        buf.append( ws + "</LineOfInterest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        LineOfInterest o = (LineOfInterest) anotherObj;
        if (LineID != o.LineID) return false;
         if (!Line.equals( o.Line)) return false;
        if (LineAction != o.LineAction) return false;
        if (LineLabel == null && o.LineLabel != null) return false;
        if ( LineLabel!= null && !LineLabel.equals(o.LineLabel)) return false;
        if (BackgroundBehaviorLine != o.BackgroundBehaviorLine) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
