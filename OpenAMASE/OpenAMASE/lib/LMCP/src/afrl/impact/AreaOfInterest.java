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
 Area of interest 
*/
public class AreaOfInterest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 21;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "AreaOfInterest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.AreaOfInterest";

    /**  ID for area of interest (Units: None)*/
    @LmcpType("int64")
    protected long AreaID = 0L;
    /**  Geometry describing the area. A valid AreaOfInterest must define Area (null not allowed). (Units: None)*/
    @LmcpType("AbstractGeometry")
    protected afrl.cmasi.AbstractGeometry Area = new afrl.cmasi.AbstractGeometry();
    /**  Action that updated this area of interest (e.g. created, destroyed, modified) (Units: None)*/
    @LmcpType("AreaActionOptions")
    protected afrl.impact.AreaActionOptions AreaAction = afrl.impact.AreaActionOptions.Created;
    /**  Human readable label for area of interest (Units: None)*/
    @LmcpType("string")
    protected String AreaLabel = "";
    /**  Background Behavior area is true if point is for background behavior (Units: None)*/
    @LmcpType("bool")
    protected boolean BackgroundBehaviorArea = false;

    
    public AreaOfInterest() {
    }

    public AreaOfInterest(long AreaID, afrl.cmasi.AbstractGeometry Area, afrl.impact.AreaActionOptions AreaAction, String AreaLabel, boolean BackgroundBehaviorArea){
        this.AreaID = AreaID;
        this.Area = Area;
        this.AreaAction = AreaAction;
        this.AreaLabel = AreaLabel;
        this.BackgroundBehaviorArea = BackgroundBehaviorArea;
    }


    public AreaOfInterest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AreaOfInterest newObj = new AreaOfInterest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID for area of interest (Units: None)*/
    public long getAreaID() { return AreaID; }

    /**  ID for area of interest (Units: None)*/
    public AreaOfInterest setAreaID( long val ) {
        AreaID = val;
        return this;
    }

    /**  Geometry describing the area. A valid AreaOfInterest must define Area (null not allowed). (Units: None)*/
    public afrl.cmasi.AbstractGeometry getArea() { return Area; }

    /**  Geometry describing the area. A valid AreaOfInterest must define Area (null not allowed). (Units: None)*/
    public AreaOfInterest setArea( afrl.cmasi.AbstractGeometry val ) {
        Area = val;
        return this;
    }

    /**  Action that updated this area of interest (e.g. created, destroyed, modified) (Units: None)*/
    public afrl.impact.AreaActionOptions getAreaAction() { return AreaAction; }

    /**  Action that updated this area of interest (e.g. created, destroyed, modified) (Units: None)*/
    public AreaOfInterest setAreaAction( afrl.impact.AreaActionOptions val ) {
        AreaAction = val;
        return this;
    }

    /**  Human readable label for area of interest (Units: None)*/
    public String getAreaLabel() { return AreaLabel; }

    /**  Human readable label for area of interest (Units: None)*/
    public AreaOfInterest setAreaLabel( String val ) {
        AreaLabel = val;
        return this;
    }

    /**  Background Behavior area is true if point is for background behavior (Units: None)*/
    public boolean getBackgroundBehaviorArea() { return BackgroundBehaviorArea; }

    /**  Background Behavior area is true if point is for background behavior (Units: None)*/
    public AreaOfInterest setBackgroundBehaviorArea( boolean val ) {
        BackgroundBehaviorArea = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 13; // accounts for primitive types
        size += LMCPUtil.sizeOf(Area);
        size += LMCPUtil.sizeOfString(AreaLabel);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        AreaID = LMCPUtil.getInt64(in);

            Area = (afrl.cmasi.AbstractGeometry) LMCPUtil.getObject(in);
        AreaAction = afrl.impact.AreaActionOptions.unpack( in );

        AreaLabel = LMCPUtil.getString(in);

        BackgroundBehaviorArea = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, AreaID);
        LMCPUtil.putObject(out, Area);
        AreaAction.pack(out);
        LMCPUtil.putString(out, AreaLabel);
        LMCPUtil.putBool(out, BackgroundBehaviorArea);

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
        buf.append( ws + "<AreaOfInterest Series=\"IMPACT\">\n");
        buf.append( ws + "  <AreaID>" + String.valueOf(AreaID) + "</AreaID>\n");
        if (Area!= null){
           buf.append( ws + "  <Area>\n");
           buf.append( ( Area.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Area>\n");
        }
        buf.append( ws + "  <AreaAction>" + String.valueOf(AreaAction) + "</AreaAction>\n");
        buf.append( ws + "  <AreaLabel>" + String.valueOf(AreaLabel) + "</AreaLabel>\n");
        buf.append( ws + "  <BackgroundBehaviorArea>" + String.valueOf(BackgroundBehaviorArea) + "</BackgroundBehaviorArea>\n");
        buf.append( ws + "</AreaOfInterest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AreaOfInterest o = (AreaOfInterest) anotherObj;
        if (AreaID != o.AreaID) return false;
        if (Area == null && o.Area != null) return false;
        if ( Area!= null && !Area.equals(o.Area)) return false;
        if (AreaAction != o.AreaAction) return false;
        if (AreaLabel == null && o.AreaLabel != null) return false;
        if ( AreaLabel!= null && !AreaLabel.equals(o.AreaLabel)) return false;
        if (BackgroundBehaviorArea != o.BackgroundBehaviorArea) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
